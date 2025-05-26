package com.example.campus_life_assistant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.campus_life_assistant.model.ChargeHistory;
import com.example.campus_life_assistant.model.Dormitory;
import com.example.campus_life_assistant.network.ApiService;
import com.example.campus_life_assistant.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuSheElectricityChargeActivity extends AppCompatActivity {

    private EditText etAmount;
    private TextView tvBalance;
    private LinearLayout historyContainer; // 新增容器引用
    // 将 balance 提升为类的成员变量
    private double balance = 50.0; // 模拟初始余额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sushe_electricity_charge); // 加载充电费页面布局

        etAmount = findViewById(R.id.etAmount);
        tvBalance = findViewById(R.id.tvBalance);
        Button btnCharge = findViewById(R.id.btnCharge);
        // 获取历史记录容器
        CardView cvHistory = findViewById(R.id.cvHistory);
        LinearLayout linearLayout = (LinearLayout) cvHistory.getChildAt(0);
        historyContainer = linearLayout;

        // 设置充值按钮点击事件
        btnCharge.setOnClickListener(v -> {
            String inputAmount = etAmount.getText().toString().trim();
            if (inputAmount.isEmpty()) {
                Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(inputAmount);
                if (amount < 10) {
                    Toast.makeText(this, "充值金额必须大于等于 ¥10.00", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 更新余额
                balance += amount;
                updateBalance(balance);

                // 清空输入框
                etAmount.setText("");

                // 显示成功提示
                Toast.makeText(this, "充值成功！当前余额：¥" + balance, Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
            }
        });
        loadChargeHistory();
        loadCurrentBalance();
    }

    // 更新余额显示
    private void updateBalance(double balance) {
        tvBalance.setText("当前余额：¥" + String.format("%.2f", balance));
    }

    private void loadCurrentBalance() {
        String buildingNo = "8";   // 示例宿舍楼号
        String roomNo = "210";     // 示例宿舍号

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Dormitory> call = apiService.getBalance(buildingNo, roomNo);

        call.enqueue(new Callback<Dormitory>() {
            @Override
            public void onResponse(@NonNull Call<Dormitory> call, @NonNull Response<Dormitory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Dormitory dormitory = response.body();
                    balance = dormitory.getBalance(); // 获取余额
                    updateBalance(balance);           // 更新 UI
                } else {
                    Toast.makeText(SuSheElectricityChargeActivity.this, "获取余额失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Dormitory> call, @NonNull Throwable t) {
                Toast.makeText(SuSheElectricityChargeActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // 请求历史记录
    private void loadChargeHistory() {
        // 宿舍信息示例
        String buildingNo = "8";  // 宿舍栋号
        String roomNo = "210";     // 宿舍编号

        // 获取 Retrofit 实例并创建接口
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        // 发起异步请求
        Call<List<ChargeHistory>> call = apiService.getChargeHistory(buildingNo, roomNo);
        call.enqueue(new Callback<List<ChargeHistory>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChargeHistory>> call, @NonNull Response<List<ChargeHistory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChargeHistory> histories = response.body();

                    // 动态添加历史记录到 LinearLayout
                    for (ChargeHistory history : histories) {
                        TextView tvItem = new TextView(SuSheElectricityChargeActivity.this);
                        tvItem.setText(history.getDate() + " - ¥" + String.format("%.2f", history.getAmount()));
                        tvItem.setTextSize(14);
                        tvItem.setTextColor(getColor(android.R.color.black));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 8, 0, 4); // 上下边距
                        tvItem.setLayoutParams(params);

                        historyContainer.addView(tvItem);
                    }
                } else {
                    Toast.makeText(SuSheElectricityChargeActivity.this, "加载记录失败", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(@NonNull Call<List<ChargeHistory>> call, @NonNull Throwable t) {
                Toast.makeText(SuSheElectricityChargeActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}