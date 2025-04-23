package com.example.campus_life_assistant;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuSheAnnouncementDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sushe_announcement_detail);

        // 获取传递的数据
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String date = getIntent().getStringExtra("date");
        String content = getIntent().getStringExtra("content");

        // 初始化视图
        TextView tvDetailTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDetailAuthor = findViewById(R.id.tvDetailAuthor);
        TextView tvDetailDate = findViewById(R.id.tvDetailDate);
        TextView tvDetailContent = findViewById(R.id.tvDetailContent);

        // 设置数据
        tvDetailTitle.setText(title);
        tvDetailAuthor.setText("发布人：" + author);
        tvDetailDate.setText(date);
        tvDetailContent.setText(content);
    }
}