package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.example.campus_life_assistant.R;

import java.util.Locale;

public class CardRechargeActivity extends AppCompatActivity {

    private EditText etRechargeAmount;
    private Button btnConfirmRecharge;
    private TextView tvCardRechargeBalance;

    private static double currentBalance = 100.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_recharge);

        etRechargeAmount = findViewById(R.id.et_recharge_amount);
        btnConfirmRecharge = findViewById(R.id.btn_confirm_recharge);
        tvCardRechargeBalance = findViewById(R.id.tv_card_recharge_balance);

        updateBalanceDisplay();

        btnConfirmRecharge.setOnClickListener(v -> {
            String amountStr = etRechargeAmount.getText().toString().trim();
            if (!amountStr.isEmpty()) {
                try {
                    double rechargeAmount = Double.parseDouble(amountStr);
                    if (rechargeAmount > 0) {
                        currentBalance += rechargeAmount;
                        updateBalanceDisplay();
                        Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "充值金额必须大于0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBalanceDisplay() {
        tvCardRechargeBalance.setText(String.format(Locale.getDefault(), "%.2f元", currentBalance));
    }
} 