package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent; // 导入 Intent

import com.example.campus_life_assistant.R;

import java.math.BigDecimal;
import java.util.Locale;

// 导入 Retrofit 和相关类
import com.example.campus_life_assistant.network.CampusCardApiService;
import com.example.campus_life_assistant.campuscard.model.CampusCard; // 导入 CampusCard Model
import com.example.campus_life_assistant.campuscard.model.InternetFee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InternetFeeActivity extends AppCompatActivity {

    private TextView tvCampusCardBalance;
    private TextView tvInternetFeeBalance;
    private EditText etAmount;
    private Button btnPay;

    private CampusCardApiService campusCardApiService;
    private String cardId;
    private String userId = "2"; // 硬编码用户ID，与数据库中test_user的u_id一致

    private static final String BASE_URL = "http://10.0.2.2:8081/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_fee);

        tvCampusCardBalance = findViewById(R.id.tv_current_balance_internet_fee);
        tvInternetFeeBalance = findViewById(R.id.tv_internet_fee_balance);
        etAmount = findViewById(R.id.et_internet_fee_amount);
        btnPay = findViewById(R.id.btn_pay_internet_fee);

        // 获取从CampusCardActivity传递过来的cardId
        cardId = getIntent().getStringExtra("cardId");

        // 初始化Retrofit
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        campusCardApiService = retrofit.create(CampusCardApiService.class);

        // 加载校园卡余额和网费余额
        loadCampusCardBalance();
        loadInternetFeeBalance();

        btnPay.setOnClickListener(v -> {
            payInternetFee();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从其他Activity返回时刷新余额
        loadCampusCardBalance();
        loadInternetFeeBalance();
    }

    private void loadCampusCardBalance() {
        if (cardId == null) {
            tvCampusCardBalance.setText("当前校园卡余额: --");
            return;
        }
        campusCardApiService.getCampusCardByCardId(cardId)
                .enqueue(new Callback<com.example.campus_life_assistant.campuscard.model.CampusCard>() {
                    @Override
                    public void onResponse(Call<com.example.campus_life_assistant.campuscard.model.CampusCard> call, Response<com.example.campus_life_assistant.campuscard.model.CampusCard> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            tvCampusCardBalance.setText(String.format(Locale.getDefault(), "当前校园卡余额: %.2f", response.body().getBalance()));
                        } else {
                             String errorMessage = "获取校园卡余额失败";
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
                            Toast.makeText(InternetFeeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            tvCampusCardBalance.setText("当前校园卡余额: --");
                        }
                    }

                    @Override
                    public void onFailure(Call<com.example.campus_life_assistant.campuscard.model.CampusCard> call, Throwable t) {
                        Toast.makeText(InternetFeeActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        tvCampusCardBalance.setText("当前校园卡余额: --");
                    }
                });
    }

    private void loadInternetFeeBalance() {
        // 使用硬编码的用户ID获取网费余额
        campusCardApiService.getInternetFeeByUserId(userId)
                .enqueue(new Callback<InternetFee>() {
                    @Override
                    public void onResponse(Call<InternetFee> call, Response<InternetFee> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            tvInternetFeeBalance.setText(String.format(Locale.getDefault(), "当前网费余额: %.2f", response.body().getBalance()));
                        } else {
                            String errorMessage = "获取网费余额失败";
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
                            Toast.makeText(InternetFeeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            tvInternetFeeBalance.setText("当前网费余额: --");
                        }
                    }

                    @Override
                    public void onFailure(Call<InternetFee> call, Throwable t) {
                        Toast.makeText(InternetFeeActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        tvInternetFeeBalance.setText("当前网费余额: --");
                    }
                });
    }

    private void payInternetFee() {
        String amountStr = etAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "请输入支付金额", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                Toast.makeText(this, "支付金额必须大于零", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardId == null) {
            Toast.makeText(this, "校园卡信息未加载，无法支付", Toast.LENGTH_SHORT).show();
            return;
        }

        campusCardApiService.payInternetFee(cardId, amount)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InternetFeeActivity.this, "网费支付成功", Toast.LENGTH_SHORT).show();
                            // 支付成功后刷新校园卡余额和网费余额
                            loadCampusCardBalance();
                            loadInternetFeeBalance();
                            etAmount.setText(""); // 清空输入框
                        } else {
                            String errorMessage = "卡已经挂失，网费支付失败";
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
                            Toast.makeText(InternetFeeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(InternetFeeActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
} 