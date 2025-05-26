package com.example.campus_life_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.campus_life_assistant.model.ChargeRequest;
import com.example.campus_life_assistant.model.ChargeResponse;
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

                // 获取 buildingNo 和 roomNo（可以从 SharedPreferences 中读取）
                SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
                String buildingNo = sharedPref.getString("buildingNo", null);
                String roomNo = sharedPref.getString("roomNo", null);

                if (buildingNo == null || roomNo == null) {
                    Toast.makeText(this, "未绑定宿舍信息，请先选择宿舍", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, SelectDormitoryActivity.class));
                    finish();
                    return;
                }

                // 获取用户名作为充值人姓名（可从 SharedPreferences 中获取）
                String username = sharedPref.getString("username", null);

                // 构造请求体
                ChargeRequest request = new ChargeRequest(buildingNo, roomNo, amount, username);

                // 发起网络请求
                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Call<ChargeResponse> call = apiService.chargeElectricity(request);

                call.enqueue(new Callback<ChargeResponse>() {
                    @Override
                    public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            // 获取服务器返回的余额和记录
                            double serverBalance = response.body().getBalance();
                            ChargeHistory latestHistory = response.body().getHistory();

                            // 更新本地余额
                            balance = serverBalance;
                            updateBalance(balance);

                            // 在页面上展示最新记录（可选）
                            showLatestCharge(latestHistory);

                            Toast.makeText(SuSheElectricityChargeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SuSheElectricityChargeActivity.this, "充值失败：" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChargeResponse> call, Throwable t) {
                        Toast.makeText(SuSheElectricityChargeActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (NumberFormatException e) {
                Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
            }
        });
        loadChargeHistory();
        loadCurrentBalance();
    }
    private void showLatestCharge(ChargeHistory history) {
        TextView tvItem = new TextView(this);
        tvItem.setText(history.getDate() + " - ¥" + history.getAmount() + "（" + history.getName() + "）");
        tvItem.setTextSize(14);
        tvItem.setTextColor(getColor(android.R.color.black));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 4); // 上下边距
        tvItem.setLayoutParams(params);

        historyContainer.addView(tvItem, 0); // 插入到开头
    }
    // 更新余额显示
    private void updateBalance(double balance) {
        tvBalance.setText("当前余额：¥" + String.format("%.2f", balance));
    }

    private void loadCurrentBalance() {
        SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String buildingNo = sharedPref.getString("buildingNo", null);
        String roomNo = sharedPref.getString("roomNo", null);

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
        SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String buildingNo = sharedPref.getString("buildingNo", null);
        String roomNo = sharedPref.getString("roomNo", null);

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