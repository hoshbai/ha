package com.example.campus_life_assistant;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.campus_life_assistant.entry.CampusEvent;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class EventDetailActivity extends AppCompatActivity {

    private CampusEvent event;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // 初始化视图
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        // 获取传递的活动数据
        if (getIntent().hasExtra("event")) {
            event = (CampusEvent) getIntent().getSerializableExtra("event");
        } else {
            Toast.makeText(this, "未找到活动数据", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 设置标题
        collapsingToolbar.setTitle(event.getTitle());

        // 填充活动详情
        TextView timeTextView = findViewById(R.id.event_detail_time);
        TextView locationTextView = findViewById(R.id.event_detail_location);
        TextView organizerTextView = findViewById(R.id.event_detail_organizer);
        TextView categoryTextView = findViewById(R.id.event_detail_category);
        TextView descriptionTextView = findViewById(R.id.event_detail_description);
        ImageView imageView = findViewById(R.id.event_detail_image);
        registerButton = findViewById(R.id.event_detail_register_button);

        // 格式化日期时间
        String startTimeStr = DateFormat.format("yyyy年MM月dd日 HH:mm", event.getStartTime()).toString();
        String endTimeStr = DateFormat.format("yyyy年MM月dd日 HH:mm", event.getEndTime()).toString();
        String timeRange = startTimeStr + " 至 " + endTimeStr;

        timeTextView.setText(timeRange);
        locationTextView.setText(event.getLocation());
        organizerTextView.setText(event.getOrganizer());
        categoryTextView.setText(event.getCategory());
        descriptionTextView.setText(event.getDescription());

        // 设置图片资源（实际应用中应该使用图片加载库如Glide）
        // 此处使用了占位图
        imageView.setImageResource(R.drawable.ic_launcher_foreground);

        // 设置报名按钮状态
        updateRegisterButtonState();

        // 设置报名按钮点击事件
        registerButton.setOnClickListener(v -> {
            if (event.isRegistered()) {
                // 已报名，取消报名
                event.setRegistered(false);
                Toast.makeText(EventDetailActivity.this, "已取消报名", Toast.LENGTH_SHORT).show();
            } else {
                // 未报名，进行报名
                event.setRegistered(true);
                Toast.makeText(EventDetailActivity.this, "报名成功！", Toast.LENGTH_SHORT).show();
            }
            // 更新按钮状态
            updateRegisterButtonState();
        });
    }

    private void updateRegisterButtonState() {
        if (event.isRegistered()) {
            registerButton.setText("取消报名");
        } else {
            registerButton.setText("报名参加");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 