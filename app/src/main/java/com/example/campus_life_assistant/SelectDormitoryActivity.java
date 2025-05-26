package com.example.campus_life_assistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.model.BasicResponse;
import com.example.campus_life_assistant.network.ApiService;
import com.example.campus_life_assistant.network.RetrofitClient;
import com.example.campus_life_assistant.model.Dormitory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectDormitoryActivity extends AppCompatActivity {

    private Spinner spBuildingNo, spRoomNo;
    private Button btnSubmit;
    private List<Dormitory> dormitoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dormitory);

        spBuildingNo = findViewById(R.id.spBuildingNo);
        spRoomNo = findViewById(R.id.spRoomNo);
        btnSubmit = findViewById(R.id.btnSubmit);

        loadDormitoryData();

        btnSubmit.setOnClickListener(v -> {
            String buildingNo = spBuildingNo.getSelectedItem().toString();
            String roomNo = spRoomNo.getSelectedItem().toString();

            SharedPreferences sharedPref = getSharedPreferences("user_session", MODE_PRIVATE);
            String username = sharedPref.getString("username", null);

            if (username == null) {
                Toast.makeText(this, "用户未登录，请重新登录", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            // 构造 Dormitory 对象作为请求体
            Dormitory dormitory = new Dormitory(buildingNo, roomNo, username);

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<Boolean> call = apiService.updateDormitory(dormitory);

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        sharedPref.edit()
                                .putString("buildingNo", buildingNo)
                                .putString("roomNo", roomNo)
                                .apply();

                        Toast.makeText(SelectDormitoryActivity.this, "已选择宿舍：" + buildingNo + "-" + roomNo, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SelectDormitoryActivity.this, SuSheMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 清除栈中旧页面
                        startActivity(intent);
                        finish(); // 返回上一页
                    } else {
                        Toast.makeText(SelectDormitoryActivity.this, "提交失败：", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(SelectDormitoryActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void loadDormitoryData() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Dormitory>> call = apiService.getAllDormitories();

        call.enqueue(new Callback<List<Dormitory>>() {
            @Override
            public void onResponse(Call<List<Dormitory>> call, Response<List<Dormitory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dormitoryList = response.body();
                    updateSpinners();
                } else {
                    Toast.makeText(SelectDormitoryActivity.this, "加载宿舍失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Dormitory>> call, Throwable t) {
                Toast.makeText(SelectDormitoryActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateSpinners() {
        List<String> buildingNos = new ArrayList<>();

        for (Dormitory d : dormitoryList) {
            if (!buildingNos.contains(d.getBuildingNo())) {
                buildingNos.add(d.getBuildingNo());
            }
        }

        // 设置楼号 Spinner
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, buildingNos);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBuildingNo.setAdapter(buildingAdapter);

        // 设置楼号选择监听器
        spBuildingNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBuilding = buildingNos.get(position);
                List<String> rooms = new ArrayList<>();

                // 筛选对应房间号
                for (Dormitory d : dormitoryList) {
                    if (d.getBuildingNo().equals(selectedBuilding)) {
                        rooms.add(d.getRoomNo());
                    }
                }

                // 更新房号 Spinner
                ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(SelectDormitoryActivity.this,
                        android.R.layout.simple_spinner_item, rooms);
                roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRoomNo.setAdapter(roomAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 可选：不选择时的逻辑
                spRoomNo.setAdapter(null); // 清空房号
            }
        });
    }
}