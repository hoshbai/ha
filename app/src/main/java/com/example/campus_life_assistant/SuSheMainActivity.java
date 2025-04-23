package com.example.campus_life_assistant;
import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Locale;

public class SuSheMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sushe_main);

        // 初始化动态内容
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvNotice = findViewById(R.id.tvNotice);

        // 设置当前日期
        String currentDate = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).format(new Date());
        tvDate.setText(currentDate);

        // 设置默认欢迎语（后期可替换为真实数据）
        tvWelcome.setText("欢迎回来，\n李四");

        // 设置示例公告（后期可替换为网络数据）
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
}