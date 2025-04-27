package com.example.campus_life_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.Adapter.FoodAdapter;
import com.example.campus_life_assistant.entry.Canteen;
import com.example.campus_life_assistant.entry.Food;
import com.example.campus_life_assistant.manager.CanteenManager;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class CanteenActivity extends AppCompatActivity {

    private Spinner canteenSpinner;
    private TextView tvOpeningHours;
    private TextView tvQueueStatus;
    private LinearProgressIndicator queueIndicator;
    private RecyclerView rvRecommendations;
    private RecyclerView rvMenu;
    private FoodAdapter recommendationAdapter;
    private FoodAdapter menuAdapter;
    private CanteenManager canteenManager;
    private List<Canteen> canteens;
    private List<String> canteenNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen);

        // 初始化视图
        initViews();

        // 初始化数据
        canteenManager = CanteenManager.getInstance(this);
        canteens = canteenManager.getAllCanteens();

        // 设置食堂选择器
        setupCanteenSpinner();

        // 设置RecyclerView
        setupRecyclerViews();
    }

    private void initViews() {
        canteenSpinner = findViewById(R.id.canteen_spinner);
        tvOpeningHours = findViewById(R.id.tv_opening_hours);
        tvQueueStatus = findViewById(R.id.tv_queue_status);
        queueIndicator = findViewById(R.id.queue_indicator);
        rvRecommendations = findViewById(R.id.rv_recommendations);
        rvMenu = findViewById(R.id.rv_menu);
    }

    private void setupCanteenSpinner() {
        // 提取食堂名称
        canteenNames = new ArrayList<>();
        for (Canteen canteen : canteens) {
            canteenNames.add(canteen.getName());
        }

        // 设置适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, canteenNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        canteenSpinner.setAdapter(adapter);

        // 设置选择监听器
        canteenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateCanteenInfo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 什么都不做
            }
        });
    }

    private void setupRecyclerViews() {
        // 设置推荐菜品RecyclerView（横向滚动）
        LinearLayoutManager recommendationLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        rvRecommendations.setLayoutManager(recommendationLayoutManager);

        recommendationAdapter = new FoodAdapter(
                this, new ArrayList<>(), true);
        rvRecommendations.setAdapter(recommendationAdapter);

        // 设置菜单RecyclerView（垂直列表）
        LinearLayoutManager menuLayoutManager = new LinearLayoutManager(this);
        rvMenu.setLayoutManager(menuLayoutManager);

        menuAdapter = new FoodAdapter(
                this, new ArrayList<>(), false);
        rvMenu.setAdapter(menuAdapter);

        // 初始显示第一个食堂信息
        if (!canteens.isEmpty()) {
            updateCanteenInfo(0);
        }
    }

    private void updateCanteenInfo(int position) {
        if (position < 0 || position >= canteens.size()) {
            return;
        }

        Canteen canteen = canteens.get(position);

        // 更新食堂信息
        tvOpeningHours.setText("营业时间: " + canteen.getOpeningHours());
        tvQueueStatus.setText("排队情况: " + canteen.getQueueStatusText());

        // 更新排队指示器
        queueIndicator.setProgress(canteen.getQueueStatus() * 20); // 转换为百分比

        // 设置指示器颜色，根据排队状态
        int queueStatus = canteen.getQueueStatus();
        if (queueStatus <= 1) {
            queueIndicator.setIndicatorColor(getResources().getColor(R.color.queue_low));
        } else if (queueStatus <= 3) {
            queueIndicator.setIndicatorColor(getResources().getColor(R.color.queue_medium));
        } else {
            queueIndicator.setIndicatorColor(getResources().getColor(R.color.queue_high));
        }

        // 更新推荐菜品
        List<Food> recommendations = canteen.getRecommendations();
        recommendationAdapter.updateData(recommendations);

        // 更新菜单
        List<Food> menu = canteen.getMenu();
        menuAdapter.updateData(menu);
    }
}