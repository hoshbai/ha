package com.example.campus_life_assistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campus_life_assistant.AIChatActivity;
import com.example.campus_life_assistant.CanteenActivity;
import com.example.campus_life_assistant.Maptest;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.RepairActivity;
import com.example.campus_life_assistant.ScheduleActivity;
import com.example.campus_life_assistant.StuCardActivity;
import com.example.campus_life_assistant.SuSheMainActivity;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载 activity_home 布局文件
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 设置功能卡片点击事件
        setupCardClickListeners(view);

        return view;
    }

    private void setupCardClickListeners(View view) {
        // 课程表卡片
        view.findViewById(R.id.scheduleCard).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent);
        });

        // 图书馆卡片
        view.findViewById(R.id.libraryCard).setOnClickListener(v -> {
            Toast.makeText(getContext(), "跳转到图书馆界面 跳转逻辑待实现", Toast.LENGTH_SHORT).show();

            // 跳转逻辑待实现
        });

        // 食堂卡片
        view.findViewById(R.id.canteenCard).setOnClickListener(v -> {
            Toast.makeText(getContext(), "跳转到食堂界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), CanteenActivity.class);
            startActivity(intent);
        });


        // 宿舍卡片
        view.findViewById(R.id.dormitoryCard).setOnClickListener(v -> {
            Toast.makeText(getContext(), "跳转到宿舍界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), SuSheMainActivity.class);
            startActivity(intent);
            // 跳转逻辑待实现
        });
        // AI聊天卡片
        view.findViewById(R.id.AICard).setOnClickListener(v -> {
            Toast.makeText(getContext(), "跳转到校园助手界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AIChatActivity.class);
            startActivity(intent);
            // 跳转逻辑待实现
        });
        // A校园地图导航卡片
        view.findViewById(R.id.SchoolMap).setOnClickListener(v -> {
            Toast.makeText(getContext(), "跳转到校园地图导航界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), Maptest.class);
            startActivity(intent);
            // 跳转逻辑待实现
        });
    }
}