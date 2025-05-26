package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_life_assistant.R;

public class LossUnlossActivity extends AppCompatActivity {

    private TextView tvCardStatus;
    private Button btnToggleLossStatus;

    private boolean isCardLost = false; // 静态校园卡状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_unloss);

        tvCardStatus = findViewById(R.id.tv_card_status);
        btnToggleLossStatus = findViewById(R.id.btn_toggle_loss_status);

        // 初始化显示状态
        updateStatusDisplay();

        btnToggleLossStatus.setOnClickListener(v -> {
            // 切换状态
            isCardLost = !isCardLost;
            // 更新显示和按钮文本
            updateStatusDisplay();
            String message = isCardLost ? "校园卡已挂失 " : "校园卡已解除挂失";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            // TODO: 在实际应用中，这里需要调用后端 API 更新卡片状态
        });
    }

    private void updateStatusDisplay() {
        if (isCardLost) {
            tvCardStatus.setText("校园卡状态: 已挂失");
            btnToggleLossStatus.setText("解除挂失");
        } else {
            tvCardStatus.setText("校园卡状态: 正常");
            btnToggleLossStatus.setText("挂失校园卡");
        }
    }
} 