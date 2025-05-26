package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.example.campus_life_assistant.R;

import java.math.BigDecimal;
import java.util.Locale;

// 导入 Retrofit 和相关类
import com.example.campus_life_assistant.network.CampusCardApiService;
import com.example.campus_life_assistant.campuscard.model.CampusCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardRechargeActivity extends AppCompatActivity {

    private TextView tvCardRechargeBalance;
    private EditText etRechargeAmount;
    private Button btnConfirmRecharge;

    private CampusCardApiService campusCardApiService;
    private static final String BASE_URL = "http://10.0.2.2:8081/api/";

    private String cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_recharge);

        if (getIntent().getExtras() != null) {
            cardId = getIntent().getExtras().getString("cardId");
            if (cardId == null) {
                 Toast.makeText(this, "未获取到校园卡ID", Toast.LENGTH_SHORT).show();
                 finish();
                 return;
            }
        } else {
            Toast.makeText(this, "未获取到校园卡信息", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvCardRechargeBalance = findViewById(R.id.tv_card_recharge_balance);
        etRechargeAmount = findViewById(R.id.et_recharge_amount);
        btnConfirmRecharge = findViewById(R.id.btn_confirm_recharge);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        campusCardApiService = retrofit.create(CampusCardApiService.class);

        loadCurrentBalance(cardId);

        btnConfirmRecharge.setOnClickListener(v -> {
            rechargeCard(cardId);
        });
    }

    // 加载当前余额的方法
    private void loadCurrentBalance(String cardId) {
        campusCardApiService.getCampusCardByCardId(cardId) // **使用根据卡ID获取信息的接口**
                .enqueue(new Callback<CampusCard>() {
                    @Override
                    public void onResponse(Call<CampusCard> call, Response<CampusCard> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            CampusCard campusCard = response.body();
                            tvCardRechargeBalance.setText(String.format(Locale.getDefault(), "%.2f元", campusCard.getBalance()));
                        } else {
                            String errorMessage = "获取当前余额失败";
                            if (response.code() != 0) {
                                errorMessage += ", 状态码: " + response.code();
                            } else if (response.body() != null) { // 有body但非成功状态码
                                 errorMessage += ", 错误信息: " + response.body(); // 尝试显示非成功body
                             }
                             if (response.errorBody() != null) {
                                try {
                                    errorMessage += ", 错误信息: " + response.errorBody().string();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(CardRechargeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            tvCardRechargeBalance.setText("--");
                        }
                    }

                    @Override
                    public void onFailure(Call<CampusCard> call, Throwable t) {
                        Toast.makeText(CardRechargeActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        tvCardRechargeBalance.setText("--");
                    }
                });
    }

    // 处理充值的方法
    private void rechargeCard(String cardId) {
        String amountStr = etRechargeAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal rechargeAmount;
        try {
            rechargeAmount = new BigDecimal(amountStr);
            if (rechargeAmount.compareTo(BigDecimal.ZERO) <= 0) {
                Toast.makeText(this, "充值金额必须大于零", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "正在充值...", Toast.LENGTH_SHORT).show();
        campusCardApiService.recharge(cardId, rechargeAmount)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(CardRechargeActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                            // 充值成功后刷新余额
                            loadCurrentBalance(cardId);
                        } else {
                            String errorMessage = "充值失败";
                             if (response.code() != 0) {
                                errorMessage += ", 状态码: " + response.code();
                            }
                            if (response.errorBody() != null) {
                                try {
                                    errorMessage += ", 错误信息: " + response.errorBody().string();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(CardRechargeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CardRechargeActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
} 