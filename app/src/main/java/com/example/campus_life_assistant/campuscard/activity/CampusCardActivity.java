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

public class CampusCardActivity extends AppCompatActivity {

    private TextView tvCampusCardBalance;
    private RecyclerView rvConsumptionRecords;
    private ConsumptionRecordAdapter consumptionRecordAdapter;
    private List<ConsumptionRecord> consumptionRecordList = new ArrayList<>();

    private Button btnRecharge;
    private Button btnLossUnloss;

    private double currentBalance = 123.45; // 静态余额
    private boolean isCardLost = false; // 静态校园卡状态，false 表示正常，true 表示已挂失

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_card);

        // 初始化视图
        tvCampusCardBalance = findViewById(R.id.tv_campus_card_balance);
        rvConsumptionRecords = findViewById(R.id.rv_consumption_records);
        btnRecharge = findViewById(R.id.btn_recharge);
        btnLossUnloss = findViewById(R.id.btn_loss_unloss);

        // 设置消费记录 RecyclerView
        rvConsumptionRecords.setLayoutManager(new LinearLayoutManager(this));
        consumptionRecordAdapter = new ConsumptionRecordAdapter(consumptionRecordList);
        rvConsumptionRecords.setAdapter(consumptionRecordAdapter);

        // 加载数据 (这里使用静态数据作为示例)
        loadCampusCardData();
        updateBalanceDisplay(); // 初始化余额显示

        // 根据静态状态更新挂失/解挂按钮文本
        updateLossUnlossButtonText();

        // 设置按钮点击事件
        btnRecharge.setOnClickListener(v -> {
            // TODO: Implement recharge logic
            showRechargeDialog(); // 显示充值对话框
            // Toast.makeText(this, "充值功能待实现", Toast.LENGTH_SHORT).show();
        });

        btnLossUnloss.setOnClickListener(v -> {
            // TODO: Implement loss/unloss logic
            toggleCardLossStatus(); // 切换校园卡挂失状态
            // Toast.makeText(this, "挂失/解挂功能待实现", Toast.LENGTH_SHORT).show();
        });

        // TODO: Implement Recharge, Loss/Unloss features
    }

    private void loadCampusCardData() {
        // 设置静态余额
        // tvCampusCardBalance.setText("123.45元"); // 示例余额 - 移除，使用 currentBalance 变量

        // 添加静态消费记录
        consumptionRecordList.add(new ConsumptionRecord("食堂午餐", "2025-05-23 12:30", -15.00));
        consumptionRecordList.add(new ConsumptionRecord("超市购物", "2025-05-22 15:00", -20.50));
        consumptionRecordList.add(new ConsumptionRecord("图书馆打印", "2025-05-21 09:00", -2.00));
        consumptionRecordList.add(new ConsumptionRecord("充值", "2025-05-20 10:00", 50.00));
        consumptionRecordList.add(new ConsumptionRecord("食堂晚餐", "2025-05-19 18:30", -18.00));

        // 通知适配器数据已改变
        consumptionRecordAdapter.notifyDataSetChanged();
    }

    private void updateBalanceDisplay() {
        tvCampusCardBalance.setText(String.format(Locale.getDefault(), "%.2f元", currentBalance));
    }

    private void showRechargeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("充值");

        // 设置输入框
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("输入充值金额");
        builder.setView(input);

        // 设置按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            String amountStr = input.getText().toString();
            if (!amountStr.isEmpty()) {
                try {
                    double rechargeAmount = Double.parseDouble(amountStr);
                    if (rechargeAmount > 0) {
                        // 模拟充值
                        currentBalance += rechargeAmount;
                        updateBalanceDisplay();

                        // 添加充值记录
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String currentTime = sdf.format(new Date());
                        consumptionRecordList.add(0, new ConsumptionRecord("充值", currentTime, rechargeAmount)); // 添加到列表顶部
                        consumptionRecordAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "充值金额必须大于0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "充值金额不能为空", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateLossUnlossButtonText() {
        if (isCardLost) {
            btnLossUnloss.setText("解除挂失");
        } else {
            btnLossUnloss.setText("挂失校园卡");
        }
    }

    private void toggleCardLossStatus() {
        isCardLost = !isCardLost; // 切换状态
        updateLossUnlossButtonText(); // 更新按钮文本
        String message = isCardLost ? "校园卡已挂失" : "校园卡已解除挂失";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        // TODO: 在实际应用中，这里需要调用后端 API 更新卡片状态
    }
} 