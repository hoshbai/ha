package com.example.campus_life_assistant;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.campus_life_assistant.Adapter.LostFoundPagerAdapter;
import com.example.campus_life_assistant.entry.LostFoundItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LostFoundActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ProgressBar progressBar;
    private String[] tabTitles = new String[]{"全部", "寻物启事", "招领启事"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

        // 初始化视图
        initViews();
        
        // 设置工具栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // 设置ViewPager和TabLayout
        setupViewPager();
    }

    private void initViews() {
        toolbar = findViewById(R.id.lost_found_toolbar);
        tabLayout = findViewById(R.id.lost_found_tab_layout);
        viewPager = findViewById(R.id.lost_found_view_pager);
        progressBar = findViewById(R.id.lost_found_progress_bar);
    }
    
    private void setupViewPager() {
        // 创建适配器
        LostFoundPagerAdapter adapter = new LostFoundPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        // 连接TabLayout和ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabTitles[position]);
        }).attach();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // 显示加载指示器
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }
    
    // 隐藏加载指示器
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
} 