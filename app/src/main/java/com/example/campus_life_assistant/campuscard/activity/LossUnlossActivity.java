package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.network.CampusCardApiService;
import com.example.campus_life_assistant.campuscard.model.CampusCard;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LossUnlossActivity extends AppCompatActivity {

    private TextView tvCardStatus;
    private Button btnReportLoss;
    private Button btnUnreportLoss;

    private CampusCardApiService campusCardApiService;
    private String cardId;

    private static final String BASE_URL = "http://10.0.2.2:8081/api/";

    // 定义卡片状态常量 (与后端一致)
    private static final int STATUS_NORMAL = 0;
    private static final int STATUS_LOST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_unloss);

        tvCardStatus = findViewById(R.id.tv_card_status);
        btnReportLoss = findViewById(R.id.btn_report_loss);
        btnUnreportLoss = findViewById(R.id.btn_unreport_loss);

        // 获取从CampusCardActivity传递过来的cardId
        cardId = getIntent().getStringExtra("cardId");
        if (cardId == null) {
            Toast.makeText(this, "未获取到校园卡ID", Toast.LENGTH_SHORT).show();
            finish(); // 如果没有卡ID，关闭当前Activity
            return; // 退出onCreate
        }

        // 初始化Retrofit
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        campusCardApiService = retrofit.create(CampusCardApiService.class);

        // 加载当前卡片状态
        loadCardStatus();

        // 设置按钮点击事件
        btnReportLoss.setOnClickListener(v -> reportLoss());
        btnUnreportLoss.setOnClickListener(v -> unreportLoss());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从其他Activity返回时刷新状态
        loadCardStatus();
    }

    private void loadCardStatus() {
        if (cardId == null) {
             tvCardStatus.setText("当前卡片状态: --");
             return;
        }
        campusCardApiService.getCampusCardByCardId(cardId)
                .enqueue(new Callback<CampusCard>() {
                    @Override
                    public void onResponse(Call<CampusCard> call, Response<CampusCard> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            CampusCard campusCard = response.body();
                            try {
                                // 尝试将获取到的状态转换为 int
                                int status = Integer.parseInt(campusCard.getStatus());
                                updateStatusText(status);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                tvCardStatus.setText("当前卡片状态: 数据异常");
                                Toast.makeText(LossUnlossActivity.this, "卡片状态数据格式错误", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                             String errorMessage = "获取卡片状态失败";
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
                            Toast.makeText(LossUnlossActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            tvCardStatus.setText("当前卡片状态: --");
                        }
                    }

                    @Override
                    public void onFailure(Call<CampusCard> call, Throwable t) {
                        Toast.makeText(LossUnlossActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        tvCardStatus.setText("当前卡片状态: --");
                    }
                });
    }

    private void reportLoss() {
        if (cardId == null) {
            Toast.makeText(this, "校园卡信息未加载，无法挂失", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "正在挂失...", Toast.LENGTH_SHORT).show();
        campusCardApiService.reportLoss(cardId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LossUnlossActivity.this, "挂失成功", Toast.LENGTH_SHORT).show();
                            loadCardStatus(); // 刷新卡片状态显示
                        } else {
                            String errorMessage = "挂失失败";
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
                            Toast.makeText(LossUnlossActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LossUnlossActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unreportLoss() {
        if (cardId == null) {
            Toast.makeText(this, "校园卡信息未加载，无法解挂", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "正在解挂...", Toast.LENGTH_SHORT).show();
         campusCardApiService.unreportLoss(cardId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LossUnlossActivity.this, "解挂成功", Toast.LENGTH_SHORT).show();
                            loadCardStatus(); // 刷新卡片状态显示
                        } else {
                            String errorMessage = "解挂失败";
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
                            Toast.makeText(LossUnlossActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LossUnlossActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateStatusText(int status) {
        if (status == STATUS_NORMAL) {
            tvCardStatus.setText("当前卡片状态: 正常");
        } else if (status == STATUS_LOST) {
            tvCardStatus.setText("当前卡片状态: 已挂失");
        } else {
            tvCardStatus.setText("当前卡片状态: 未知");
        }
    }
} 