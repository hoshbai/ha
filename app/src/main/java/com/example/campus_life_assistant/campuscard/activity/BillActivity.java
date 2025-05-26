package com.example.campus_life_assistant.campuscard.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.example.campus_life_assistant.R;
import com.example.campus_life_assistant.campuscard.model.CardTransaction;
import com.example.campus_life_assistant.campuscard.ui.ConsumptionRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.campus_life_assistant.network.CampusCardApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BillActivity extends AppCompatActivity {

    private RecyclerView rvConsumptionRecords;
    private ConsumptionRecordAdapter adapter;
    private List<CardTransaction> transactionList = new ArrayList<>();

    private CampusCardApiService campusCardApiService;
    private static final String BASE_URL = "http://10.0.2.2:8081/api/";

    private String cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

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

        rvConsumptionRecords = findViewById(R.id.rv_consumption_records);
        rvConsumptionRecords.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ConsumptionRecordAdapter(transactionList);
        rvConsumptionRecords.setAdapter(adapter);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        campusCardApiService = retrofit.create(CampusCardApiService.class);

        loadTransactions(cardId);
    }

    private void loadTransactions(String cardId) {
        campusCardApiService.getTransactionsByCardId(cardId)
                .enqueue(new Callback<List<CardTransaction>>() {
                    @Override
                    public void onResponse(Call<List<CardTransaction>> call, Response<List<CardTransaction>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            transactionList.clear();
                            transactionList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            if (transactionList.isEmpty()) {
                                Toast.makeText(BillActivity.this, "暂无交易记录", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String errorMessage = "获取交易记录失败";
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
                            Toast.makeText(BillActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CardTransaction>> call, Throwable t) {
                        Toast.makeText(BillActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
} 