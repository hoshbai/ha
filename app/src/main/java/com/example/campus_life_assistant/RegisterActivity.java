package com.example.campus_life_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.Dao.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterUsername, etRegisterPassword, etConfirmPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 绑定控件
        etRegisterUsername = findViewById(R.id.et_register_username);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        dbHelper = new DatabaseHelper(this);

        // 注册按钮点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etRegisterUsername.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "密码和确认密码不匹配", Toast.LENGTH_SHORT).show();
                } else {
                    userRegisterRequest(username, password);
                }
            }
        });
    }

    private void userRegisterRequest(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        // 发送注册请求的方法
        String url = "http://10.0.2.2:8081/api/register";  // 后端注册接口地址

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("username", username);
            jsonParam.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonParam.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);

                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            finish(); // 关闭当前注册页面
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "解析响应失败", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {

                    try {
                        String responseData = null;
                        if (response.body() != null) {
                            responseData = response.body().string();
                        }
                        JSONObject jsonObject = new JSONObject(responseData);
                        String message = jsonObject.getString("message");

                        if ("用户名已被注册，请换一个用户名".equals(message)) {
                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "该用户名已被注册，请换一个用户名", Toast.LENGTH_LONG).show();
                            });
                        } else {
                            String finalMessage = message;
                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "服务器返回：" + finalMessage, Toast.LENGTH_LONG).show();
                            });
                        }
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

}
