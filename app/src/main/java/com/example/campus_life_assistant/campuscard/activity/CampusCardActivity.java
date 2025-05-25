package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.campuscard.model.ConsumptionRecord;
import com.example.campus_life_assistant.campuscard.ui.ConsumptionRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.text.InputType;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

// 导入 MaterialCardView
import com.google.android.material.card.MaterialCardView;

public class CampusCardActivity extends AppCompatActivity {

    // 添加新的功能按钮变量
    private MaterialCardView btnBill;
    private MaterialCardView btnCardRecharge;
    private MaterialCardView btnInternetFee;
    private MaterialCardView btnLossUnloss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_card);

        // 初始化新的功能按钮视图
        btnBill = findViewById(R.id.btn_bill);
        btnCardRecharge = findViewById(R.id.btn_card_recharge);
        btnInternetFee = findViewById(R.id.btn_internet_fee);
        btnLossUnloss = findViewById(R.id.btn_loss_unloss);

        // 设置功能按钮点击事件
        btnBill.setOnClickListener(v -> {
            // TODO: 跳转到账单页面
            // Toast.makeText(this, "跳转到账单页面 (待实现)", Toast.LENGTH_SHORT).show();
            android.content.Intent intent = new android.content.Intent(this, com.example.campus_life_assistant.campuscard.activity.BillActivity.class);
            startActivity(intent);
        });

        btnCardRecharge.setOnClickListener(v -> {
            // TODO: 跳转到卡片充值页面
            // Toast.makeText(this, "跳转到卡片充值页面 (待实现)", Toast.LENGTH_SHORT).show();
            android.content.Intent intent = new android.content.Intent(this, com.example.campus_life_assistant.campuscard.activity.CardRechargeActivity.class);
            startActivity(intent);
        });

        btnInternetFee.setOnClickListener(v -> {
            // TODO: 跳转到网费页面
            android.content.Intent intent = new android.content.Intent(this, com.tencent.campuslife.activity.campuscard.activity.InternetFeeActivity.class);
            startActivity(intent);
        });

        btnLossUnloss.setOnClickListener(v -> {
            // TODO: 跳转到挂失·解挂页面
            // Toast.makeText(this, "跳转到挂失·解挂页面 (待实现)", Toast.LENGTH_SHORT).show();
            android.content.Intent intent = new android.content.Intent(this, com.example.campus_life_assistant.campuscard.activity.LossUnlossActivity.class);
            startActivity(intent);
        });

        // 移除旧的加载数据和更新余额显示的代码
        // loadCampusCardData();
    }
} 