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

import com.example.campus_life_assistant.CanteenActivity;
import com.example.campus_life_assistant.LibraryActivity;
import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.ScheduleActivity;
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
            Intent intent = new Intent(getActivity(), LibraryActivity.class);
            startActivity(intent);
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
    }
}