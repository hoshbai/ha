package com.example.campus_life_assistant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.campus_life_assistant.Dao.DatabaseHelper;
import com.example.campus_life_assistant.fragment.HomeFragment;
import com.example.campus_life_assistant.fragment.ProfileFragment;
import com.example.campus_life_assistant.fragment.SchoolFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private TextView connectionStatusText;
    private ProgressBar connectionProgressBar;
    private Button retryButton;
    private Button tryLocalButton;
    private View statusContainer;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        String token = sharedPref.getString("token", null);
        if (username == null || token == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        View statusPanel = findViewById(R.id.connection_status_panel);
        if (statusPanel != null) {
            statusPanel.setVisibility(View.GONE);
        }

        dbHelper = new DatabaseHelper(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        connectionStatusText = findViewById(R.id.connection_status_text);
        connectionProgressBar = findViewById(R.id.connection_progress);
        retryButton = findViewById(R.id.retry_button);
        tryLocalButton = findViewById(R.id.try_local_button);

        if (statusContainer == null || connectionStatusText == null || connectionProgressBar == null || retryButton == null || tryLocalButton == null) {
            Log.w(TAG, "One or more connection status views not found in layout. Status display might be limited.");
        }

        if (retryButton != null) {
            retryButton.setOnClickListener(v -> attemptConnectionWithNetworkCheck());
        }
        if (tryLocalButton != null) {
            tryLocalButton.setOnClickListener(v -> tryLocalConnection());
        }

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.nav_school) {
                selectedFragment = new SchoolFragment();
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        attemptConnectionWithNetworkCheck();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    private void attemptConnectionWithNetworkCheck() {
        if (!isNetworkAvailable()) {
            Log.w(TAG, "网络连接检查失败。");
            updateConnectionStatusUI("无网络连接", false);
            showConnectionErrorDialog("网络错误", "设备当前没有连接到网络。\n请检查您的 Wi-Fi 或移动数据连接。");
            return;
        }
        testDatabaseConnection();
    }

    @SuppressLint("MissingPermission")
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            @SuppressWarnings("deprecation")
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
    }

    private void testDatabaseConnection() {
        Log.i(TAG, "开始执行主数据库连接测试...");
        updateConnectionStatusUI("正在连接主数据库...", true);

        executorService.execute(() -> {
            final DatabaseHelper.ConnectionResult result = dbHelper.testConnection();
            runOnUiThread(() -> {
                updateConnectionStatusUI(result.status + (result.success ? " ✓" : " ✗"), false);
                if (result.success) {
                    Log.i(TAG, "主数据库连接成功。");
                    showConnectionToast("主数据库连接成功");
                } else {
                    Log.e(TAG, "主数据库连接失败。 Status: " + result.status + ", Details: " + result.details, result.exception);
                    attemptLocalConnectionAutomatically(); // 自动尝试本地连接
                }
            });
        });
    }

    private void attemptLocalConnectionAutomatically() {
        Log.i(TAG, "开始自动尝试本地数据库连接...");
        updateConnectionStatusUI("正在尝试本地连接...", true);

        executorService.execute(() -> {
            final DatabaseHelper.ConnectionResult localResult = dbHelper.tryLocalConnection();
            runOnUiThread(() -> {
                updateConnectionStatusUI("本地连接: " + localResult.status + (localResult.success ? " ✓" : " ✗"), false);
                if (localResult.success) {
                    Log.i(TAG, "自动本地连接成功");
                    View statusPanel = findViewById(R.id.connection_status_panel);
                    if (statusPanel != null) {
                        statusPanel.setVisibility(View.GONE);
                    }
                } else {
                    Log.e(TAG, "自动本地连接失败");
                    showConnectionErrorDialog(localResult);
                }
            });
        });
    }

    private void tryLocalConnection() {
        Log.i(TAG, "开始执行本地数据库连接测试...");
        updateConnectionStatusUI("正在尝试本地连接...", true);
        executorService.execute(() -> {
            final DatabaseHelper.ConnectionResult result = dbHelper.tryLocalConnection();
            runOnUiThread(() -> {
                updateConnectionStatusUI("本地连接: " + result.status + (result.success ? " ✓" : " ✗"), false);
                if (result.success) {
                    Log.i(TAG, "本地数据库连接成功。");
                    showConnectionDialog("本地连接成功", result.details);
                } else {
                    Log.e(TAG, "本地数据库连接失败。 Status: " + result.status + ", Details: " + result.details, result.exception);
                    showConnectionErrorDialog(result);
                }
            });
        });
    }

    private void updateConnectionStatusUI(String status, boolean inProgress) {
        Log.d(TAG, "更新连接状态 UI: Status='" + status + "', InProgress=" + inProgress);
        if (connectionStatusText != null) {
            connectionStatusText.setText(status);
        }
        if (connectionProgressBar != null) {
            connectionProgressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        }
        if (retryButton != null) {
            retryButton.setEnabled(!inProgress);
        }
        if (tryLocalButton != null) {
            tryLocalButton.setEnabled(!inProgress);
        }
    }

    private void showConnectionErrorDialog(@NonNull DatabaseHelper.ConnectionResult result) {
        showConnectionErrorDialog(result.status, result.details);
    }

    private void showConnectionErrorDialog(String title, String message) {
        if (isFinishing() || isDestroyed()) {
            Log.w(TAG, "Activity is finishing, cannot show error dialog.");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("连接错误: " + title)
                .setMessage(message)
                .setPositiveButton("重试主连接", (dialog, which) -> attemptConnectionWithNetworkCheck())
                .setNeutralButton("尝试本地连接", (dialog, which) -> tryLocalConnection())
                .setNegativeButton("确定", null)
                .setCancelable(false)
                .show();
    }

    private void showConnectionDialog(String title, String message) {
        if (isFinishing() || isDestroyed()) {
            Log.w(TAG, "Activity is finishing, cannot show info dialog.");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }

    private void showConnectionToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            Log.d(TAG, "正在关闭 ExecutorService...");
            executorService.shutdown();
        }
    }
}