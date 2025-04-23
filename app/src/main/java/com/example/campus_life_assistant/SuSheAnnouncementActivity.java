package com.example.campus_life_assistant;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.Adapter.AnnouncementAdapter;
import com.example.campus_life_assistant.entry.Announcement;

import java.util.ArrayList;
import java.util.List;

public class SuSheAnnouncementActivity extends AppCompatActivity {

    private RecyclerView rvAnnouncements;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sushe_announcement);

        // 初始化视图
        rvAnnouncements = findViewById(R.id.rvAnnouncements);
        rvAnnouncements.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AnnouncementAdapter(announcements, this::onAnnouncementClick);
        rvAnnouncements.setAdapter(adapter);

        // 模拟加载数据
        loadAnnouncements();
    }

    private void loadAnnouncements() {
        announcements.clear();
        announcements.add(new Announcement("宿舍大扫除安排", "李四", "2023年10月1日", false, "本周六上午9点进行宿舍大扫除，请大家准时参加！"));
        announcements.add(new Announcement("项目开发规划", "张三", "2023年9月30日", true, "下周开始项目开发，请准备好相关资料。"));
        adapter.notifyDataSetChanged();
    }

    private void onAnnouncementClick(Announcement announcement) {
        Intent intent = new Intent(this, SuSheAnnouncementDetailActivity.class);
        intent.putExtra("title", announcement.getTitle());
        intent.putExtra("author", announcement.getAuthor());
        intent.putExtra("date", announcement.getDate());
        intent.putExtra("content", announcement.getContent());
        startActivity(intent);
    }
}