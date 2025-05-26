package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

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

public class CampusCardActivity extends AppCompatActivity {

    private MaterialCardView btnBill;
    private MaterialCardView btnCardRecharge;
    private MaterialCardView btnInternetFee;
    private MaterialCardView btnLossUnloss;

    private TextView tvBalance;

    private CampusCardApiService campusCardApiService;
    private static final String BASE_URL = "http://10.0.2.2:8081/api/";

    // 假设的用户ID，实际应用中应动态获取
    // private static final String CURRENT_USER_ID = "test_user"; // 注释掉用户名字段
    // private static final int CURRENT_USER_INT_ID = 2; // 注释掉硬编码整数用户ID
    private static final String CURRENT_USER_ID_STRING = "2"; // 使用硬编码整数用户ID的字符串形式，根据数据库中test_user的u_id修改

    private CampusCard currentCampusCard;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_card);

        btnBill = findViewById(R.id.btn_bill);
        btnCardRecharge = findViewById(R.id.btn_card_recharge);
        btnInternetFee = findViewById(R.id.btn_internet_fee);
        btnLossUnloss = findViewById(R.id.btn_loss_unloss);

        tvBalance = findViewById(R.id.tv_balance);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        campusCardApiService = retrofit.create(CampusCardApiService.class);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadCampusCardInfo();
                    }
                }
        );

        loadCampusCardInfo();

        btnBill.setOnClickListener(v -> {
            if (currentCampusCard != null && currentCampusCard.getCardId() != null) {
                 Intent intent = new Intent(CampusCardActivity.this, BillActivity.class);
                 intent.putExtra("cardId", currentCampusCard.getCardId());
                 startActivity(intent);
            } else {
                Toast.makeText(this, "未获取到校园卡信息，无法查看账单", Toast.LENGTH_SHORT).show();
            }
        });

        btnCardRecharge.setOnClickListener(v -> {
             if (currentCampusCard != null && currentCampusCard.getCardId() != null) {
                 Intent intent = new Intent(CampusCardActivity.this, CardRechargeActivity.class);
                 intent.putExtra("cardId", currentCampusCard.getCardId());
                 activityResultLauncher.launch(intent);
            } else {
                Toast.makeText(this, "未获取到校园卡信息，无法充值", Toast.LENGTH_SHORT).show();
            }
        });

        btnInternetFee.setOnClickListener(v -> {
             if (currentCampusCard != null && currentCampusCard.getCardId() != null) {
                Intent intent = new Intent(this, InternetFeeActivity.class);
                intent.putExtra("cardId", currentCampusCard.getCardId());
                activityResultLauncher.launch(intent);
            } else {
                Toast.makeText(this, "请先加载校园卡信息", Toast.LENGTH_SHORT).show();
            }
        });

        btnLossUnloss.setOnClickListener(v -> {
             if (currentCampusCard != null && currentCampusCard.getCardId() != null) {
                Intent intent = new Intent(this, LossUnlossActivity.class);
                intent.putExtra("cardId", currentCampusCard.getCardId());
                activityResultLauncher.launch(intent);
            } else {
                Toast.makeText(this, "请先加载校园卡信息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在 onResume 中也加载余额，确保从其他Activity返回时刷新
        loadCampusCardInfo();
    }

    private void loadCampusCardInfo() {
        // String userId = CURRENT_USER_ID; // 注释掉使用字符串用户ID
        // int userId = CURRENT_USER_INT_ID; // 注释掉使用硬编码的整数用户ID
        String userId = CURRENT_USER_ID_STRING; // 使用硬编码整数用户ID的字符串形式
        campusCardApiService.getCampusCardByUserId(userId)
                .enqueue(new Callback<CampusCard>() {
                    @Override
                    public void onResponse(Call<CampusCard> call, Response<CampusCard> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            currentCampusCard = response.body();
                            tvBalance.setText(String.format(Locale.getDefault(), "余额: %.2f", currentCampusCard.getBalance()));
                        } else {
                            String errorMessage = "获取校园卡信息失败";
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
                            Toast.makeText(CampusCardActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            tvBalance.setText("余额: --");
                        }
                    }

                    @Override
                    public void onFailure(Call<CampusCard> call, Throwable t) {
                        Toast.makeText(CampusCardActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        tvBalance.setText("余额: --");
                    }
                });
    }
} 