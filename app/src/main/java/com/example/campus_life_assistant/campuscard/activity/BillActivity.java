package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.campuscard.model.ConsumptionRecord;
import com.example.campus_life_assistant.campuscard.ui.ConsumptionRecordAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BillActivity extends AppCompatActivity {

    private RecyclerView rvConsumptionRecords;
    private ConsumptionRecordAdapter consumptionRecordAdapter;
    private List<ConsumptionRecord> consumptionRecordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // 初始化视图
        rvConsumptionRecords = findViewById(R.id.rv_consumption_records);

        // 设置消费记录 RecyclerView
        rvConsumptionRecords.setLayoutManager(new LinearLayoutManager(this));
        consumptionRecordAdapter = new ConsumptionRecordAdapter(consumptionRecordList);
        rvConsumptionRecords.setAdapter(consumptionRecordAdapter);

        // 加载静态账单数据
        loadBillData();
    }

    private void loadBillData() {
        // 添加静态消费记录 (可以复用 CampusCardActivity 中的静态数据)
        consumptionRecordList.add(new ConsumptionRecord("食堂午餐", "2025-05-23 12:30", -15.00));
        consumptionRecordList.add(new ConsumptionRecord("超市购物", "2025-05-22 15:00", -20.50));
        consumptionRecordList.add(new ConsumptionRecord("图书馆打印", "2025-05-21 09:00", -2.00));
        consumptionRecordList.add(new ConsumptionRecord("充值", "2025-05-20 10:00", 50.00));
        consumptionRecordList.add(new ConsumptionRecord("食堂晚餐", "2025-05-19 18:30", -18.00));

        // 通知适配器数据已改变
        consumptionRecordAdapter.notifyDataSetChanged();
    }
} 