package com.example.campus_life_assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.model.Dormitory;
import com.example.campus_life_assistant.network.ApiService;
import com.example.campus_life_assistant.network.RetrofitClient;

import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuSheMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDormitoryInfo(); // 先检查宿舍信
    }

    private void initUI() {
        setContentView(R.layout.activity_sushe_main);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvNotice = findViewById(R.id.tvNotice);

        // 设置当前日期
        String currentDate = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).format(new Date());
        tvDate.setText(currentDate);

        SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        tvWelcome.setText("欢迎回来，\n" + username);
        tvNotice.setText("4月15日 14:00-16:00 宿舍楼将停电检修");

        // 设置功能项点击事件
        findViewById(R.id.cvRepair).setOnClickListener(v -> {
            startActivity(new Intent(this, RepairActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        findViewById(R.id.cvCharge).setOnClickListener(v -> {
            startActivity(new Intent(this, SuSheElectricityChargeActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        findViewById(R.id.cvMembers).setOnClickListener(v -> {
            startActivity(new Intent(this, SuSheMemberInfo.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        findViewById(R.id.cvNotice).setOnClickListener(v -> {
            startActivity(new Intent(this, SuSheAnnouncementActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void checkDormitoryInfo() {
        SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        if (username == null) {
            // 用户未登录或无用户名，跳转到登录页或提示错误
            Toast.makeText(this, "用户信息异常，请重新登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class)); // 示例跳转
            finish();
            return;
        }
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Dormitory> call = apiService.getDormitoryInfo(username);

        call.enqueue(new Callback<Dormitory>() {
            @Override
            public void onResponse(Call<Dormitory> call, Response<Dormitory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Dormitory info = response.body();

                    if (info.getRoomNo() != null && info.getBuildingNo() != null) {
                        // 已填写宿舍信息，保存 buildingNo 和 roomNo
                        getSharedPreferences("user_session", MODE_PRIVATE)
                                .edit()
                                .putString("buildingNo", info.getBuildingNo())
                                .putString("roomNo", info.getRoomNo())
                                .apply();

                        // ✅ 成功获取数据后再初始化 UI
                        initUI();

                    } else {
                        // 没有宿舍信息，跳转选择页
                        startActivity(new Intent(SuSheMainActivity.this, SelectDormitoryActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(SuSheMainActivity.this, "无法获取宿舍信息", Toast.LENGTH_SHORT).show();
                    // 可选：尝试重试或进入离线模式
                }
            }

            @Override
            public void onFailure(Call<Dormitory> call, Throwable t) {
                Toast.makeText(SuSheMainActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}