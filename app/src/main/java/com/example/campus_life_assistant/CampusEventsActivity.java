package com.example.campus_life_assistant;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.campus_life_assistant.fragment.EventListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class CampusEventsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabCalendar;
    private String[] categories = new String[]{"全部", "讲座", "文娱", "体育", "竞赛", "实践"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_events);

        // 初始化工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 初始化视图
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        fabCalendar = findViewById(R.id.fab_calendar);

        // 设置ViewPager适配器
        EventCategoryAdapter adapter = new EventCategoryAdapter(this);
        viewPager.setAdapter(adapter);

        // 连接TabLayout和ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(categories[position]);
        }).attach();

        // 设置日历按钮点击事件
        fabCalendar.setOnClickListener(v -> showCalendarDialog());
    }

    // 显示日历对话框
    private void showCalendarDialog() {
        // 创建一个新的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择日期");

        // 创建一个日历视图
        CalendarView calendarView = new CalendarView(this);
        builder.setView(calendarView);

        // 设置日历选择监听器
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // 格式化日期
            String selectedDate = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
            Toast.makeText(this, "已选择: " + selectedDate, Toast.LENGTH_SHORT).show();
            
            // 查找选定日期的活动...
            // 实际应用中，这里应当进行日期过滤并跳转到相应页面或高亮显示
        });

        // 添加按钮
        builder.setPositiveButton("确定", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 显示对话框
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ViewPager适配器 - 管理不同类别的Fragment
    private class EventCategoryAdapter extends FragmentStateAdapter {

        public EventCategoryAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置创建不同类别的Fragment
            return EventListFragment.newInstance(categories[position]);
        }

        @Override
        public int getItemCount() {
            return categories.length;
        }
    }
} 