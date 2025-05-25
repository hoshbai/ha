package com.tencent.campuslife.activity.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_life_assistant.R;

public class InternetFeeActivity extends AppCompatActivity {

    private TextView tvRemainingInternetFee;
    private EditText etRechargeAmount;
    private Button btnRecharge;

    // 静态网费余额，以元为单位
    private double remainingFee = 50.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_fee);

        tvRemainingInternetFee = findViewById(R.id.tv_remaining_internet_fee);
        etRechargeAmount = findViewById(R.id.et_recharge_amount);
        btnRecharge = findViewById(R.id.btn_recharge);

        // 初始化显示剩余网费
        updateDisplay();

        btnRecharge.setOnClickListener(v -> {
            rechargeInternetFee();
        });
    }

    private void updateDisplay() {
        // 更新剩余网费显示
        tvRemainingInternetFee.setText(String.format("剩余网费: %.2f元", remainingFee));
    }

    private void rechargeInternetFee() {
        String amountStr = etRechargeAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(this, "充值金额必须大于0", Toast.LENGTH_SHORT).show();
                return;
            }

            remainingFee += amount;
            updateDisplay(); // 充值成功后更新显示
            Toast.makeText(this, "网费充值成功！", Toast.LENGTH_SHORT).show();
            etRechargeAmount.setText(""); // 清空输入框

        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }
} 