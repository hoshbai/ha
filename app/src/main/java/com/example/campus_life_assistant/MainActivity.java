package com.example.campus_life_assistant;

import android.app.AlertDialog;
import android.content.Context; // Import Context
import android.net.ConnectivityManager; // Import ConnectivityManager
import android.net.NetworkCapabilities; // Use NetworkCapabilities for modern check
import android.net.NetworkInfo; // Keep for older API levels if needed, but prefer NetworkCapabilities
import android.os.Build; // Import Build
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull; // Import NonNull
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment; // Import Fragment
import androidx.fragment.app.FragmentManager; // Import FragmentManager
import androidx.fragment.app.FragmentTransaction; // Import FragmentTransaction

import com.example.campus_life_assistant.Dao.DatabaseHelper;
import com.example.campus_life_assistant.fragment.HomeFragment;
import com.example.campus_life_assistant.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.ExecutorService; // Import ExecutorService
import java.util.concurrent.Executors; // Import Executors

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private TextView connectionStatusText;
    private ProgressBar connectionProgressBar;
    private Button retryButton;
    private Button tryLocalButton;
    private View statusContainer; // Container for status views

    // Use a single thread executor for background tasks
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this); // Initialize DatabaseHelper

        // Initialize UI elements
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        connectionStatusText = findViewById(R.id.connection_status_text);
        connectionProgressBar = findViewById(R.id.connection_progress);
        retryButton = findViewById(R.id.retry_button);
        tryLocalButton = findViewById(R.id.try_local_button);

        // Handle case where status views might not exist in layout
        if (statusContainer == null || connectionStatusText == null || connectionProgressBar == null || retryButton == null || tryLocalButton == null) {
            Log.w(TAG, "One or more connection status views not found in layout. Status display might be limited.");
            // Optionally use Snackbar as fallback (or ensure layout is correct)
            // setupTemporaryConnectionStatusViews(); // If you want Snackbar fallback
        }

        // Setup button listeners
        if (retryButton != null) {
            retryButton.setOnClickListener(v -> attemptConnectionWithNetworkCheck());
        }

        if (tryLocalButton != null) {
            tryLocalButton.setOnClickListener(v -> tryLocalConnection());
        }

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Setup bottom navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            // Add other navigation cases here...

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // Perform initial connection test on startup
        attemptConnectionWithNetworkCheck();
    }

    /**
     * Replaces the current fragment in the frame_container.
     * @param fragment The fragment to load.
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        // fragmentTransaction.addToBackStack(null); // Optional: if you want back navigation for fragments
        fragmentTransaction.commit();
    }

    /**
     * Checks for network connectivity before attempting the primary database connection.
     */
    private void attemptConnectionWithNetworkCheck() {
        if (!isNetworkAvailable()) {
            Log.w(TAG, "网络连接检查失败。"); // Network connectivity check failed.
            updateConnectionStatusUI("无网络连接", false); // No network connection
            showConnectionErrorDialog("网络错误", "设备当前没有连接到网络。\n请检查您的 Wi-Fi 或移动数据连接。");
            // Network Error, Device is not currently connected to the network. Please check Wi-Fi or mobile data.
            return; // Stop if no network
        }
        // Network available, proceed with the database test
        testDatabaseConnection();
    }

    /**
     * Checks if the device has an active network connection capable of reaching the internet.
     * Uses NetworkCapabilities for modern Android versions.
     * Requires ACCESS_NETWORK_STATE permission.
     * @return true if a network connection is available, false otherwise.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Log.e(TAG,"无法获取 ConnectivityManager"); // Cannot get ConnectivityManager
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Use NetworkCapabilities for API 23+
            android.net.Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                Log.w(TAG, "isNetworkAvailable: No active network.");
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            // Check for internet capability and validated (actual internet access)
            boolean connected = capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            Log.d(TAG, "isNetworkAvailable (API 23+): " + connected);
            return connected;

        } else { // Fallback for older APIs (less reliable for actual internet access)
            @SuppressWarnings("deprecation")
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
            Log.d(TAG, "isNetworkAvailable (API < 23): " + connected);
            return connected;
        }
    }

    /**
     * Executes the primary database connection test in a background thread.
     */
    private void testDatabaseConnection() {
        Log.i(TAG, "开始执行主数据库连接测试..."); // Starting main database connection test...
        updateConnectionStatusUI("正在连接主数据库...", true); // Connecting to main database...

        // Use the single executor service
        executorService.execute(() -> {
            final DatabaseHelper.ConnectionResult result = dbHelper.testConnection(); // Get the detailed result

            // Update UI back on the main thread
            runOnUiThread(() -> {
                updateConnectionStatusUI(result.status + (result.success ? " ✓" : " ✗"), false);
                if (result.success) {
                    Log.i(TAG, "主数据库连接成功。"); // Main database connection successful.
                    showConnectionToast("主数据库连接成功"); // Main database connection successful
                } else {
                    // Log the detailed error here as well
                    Log.e(TAG, "主数据库连接失败。 Status: " + result.status + ", Details: " + result.details, result.exception); // Main DB connection failed.
                    showConnectionErrorDialog(result); // Show detailed error dialog from result
                }
            });
        });
    }

    /**
     * Executes the local database connection test in a background thread.
     */
    private void tryLocalConnection() {
        Log.i(TAG, "开始执行本地数据库连接测试..."); // Starting local database connection test...
        updateConnectionStatusUI("正在尝试本地连接...", true); // Trying local connection...

        executorService.execute(() -> {
            final DatabaseHelper.ConnectionResult result = dbHelper.tryLocalConnection();

            runOnUiThread(() -> {
                updateConnectionStatusUI("本地连接: " + result.status + (result.success ? " ✓" : " ✗"), false); // Local connection:
                if (result.success) {
                    Log.i(TAG, "本地数据库连接成功。"); // Local database connection successful.
                    // Show details even on success for local, as it's often for debugging
                    showConnectionDialog("本地连接成功", result.details); // Local connection successful
                } else {
                    Log.e(TAG, "本地数据库连接失败。 Status: " + result.status + ", Details: " + result.details, result.exception); // Local DB connection failed.
                    showConnectionErrorDialog(result); // Show detailed error dialog
                }
            });
        });
    }

    /**
     * Updates the connection status UI elements (TextView, ProgressBar, Buttons).
     * Handles cases where views might not be found.
     */
    private void updateConnectionStatusUI(String status, boolean inProgress) {
        Log.d(TAG, "更新连接状态 UI: Status='" + status + "', InProgress=" + inProgress); // Updating connection status UI...
        if (connectionStatusText != null) {
            connectionStatusText.setText(status);
            // Optionally change text color based on success/failure/progress
            // connectionStatusText.setTextColor(getResources().getColor(inProgress ? R.color.colorPending : (status.contains("✓") ? R.color.colorSuccess : R.color.colorError)));
        } else {
            Log.w(TAG,"connectionStatusText is null, cannot update status text.");
        }

        if (connectionProgressBar != null) {
            connectionProgressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        } else {
            Log.w(TAG,"connectionProgressBar is null, cannot update progress visibility.");
        }

        // Enable/disable buttons based on progress
        if (retryButton != null) {
            retryButton.setEnabled(!inProgress);
        } else {
            Log.w(TAG, "retryButton is null, cannot update enabled state.");
        }

        if (tryLocalButton != null) {
            tryLocalButton.setEnabled(!inProgress);
        } else {
            Log.w(TAG, "tryLocalButton is null, cannot update enabled state.");
        }
    }

    /**
     * Shows a detailed connection error dialog using information from ConnectionResult.
     */
    private void showConnectionErrorDialog(@NonNull DatabaseHelper.ConnectionResult result) {
        // Use the status as title and details as message
        showConnectionErrorDialog(result.status, result.details);
    }

    /**
     * Shows a generic connection error dialog with Retry and Try Local options.
     */
    private void showConnectionErrorDialog(String title, String message) {
        if (isFinishing() || isDestroyed()) {
            Log.w(TAG, "Activity is finishing, cannot show error dialog.");
            return; // Don't show dialog if activity is finishing
        }
        new AlertDialog.Builder(this)
                .setTitle("连接错误: " + title) // Connection Error:
                .setMessage(message)
                .setPositiveButton("重试主连接", (dialog, which) -> attemptConnectionWithNetworkCheck()) // Retry Main Connection
                .setNeutralButton("尝试本地连接", (dialog, which) -> tryLocalConnection()) // Try Local Connection
                .setNegativeButton("确定", null) // OK
                .setCancelable(false) // Prevent dismissing by tapping outside
                .show();
    }

    /**
     * Shows a simple informational dialog.
     */
    private void showConnectionDialog(String title, String message) {
        if (isFinishing() || isDestroyed()) {
            Log.w(TAG, "Activity is finishing, cannot show info dialog.");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null) // OK
                .show();
    }

    /**
     * Shows a brief Toast message.
     */
    private void showConnectionToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Fallback method to show status via Snackbar if layout views are missing.
     * Not typically needed if layout is correct.
     */
    private void setupTemporaryConnectionStatusViews() {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, "正在测试数据库连接...", Snackbar.LENGTH_INDEFINITE) // Testing database connection...
                .setAction("详情", v -> { // Details
                    // Show a simple dialog initially or when details action clicked
                    showConnectionDialog("数据库连接状态", "请稍候..."); // Database Connection Status, Please wait...
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown executor service to prevent resource leaks
        if (executorService != null && !executorService.isShutdown()) {
            Log.d(TAG,"正在关闭 ExecutorService..."); // Shutting down ExecutorService...
            executorService.shutdown();
        }
    }
}