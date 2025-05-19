package com.example.campus_life_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.Dao.DatabaseHelper;
import com.example.campus_life_assistant.model.User;

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

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 绑定控件
        etLoginUsername = findViewById(R.id.et_login_username);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        dbHelper = new DatabaseHelper(this);

        // 登录按钮点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etLoginUsername.getText().toString().trim();
                String password = etLoginPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
                } else {
                    // 调用数据库方法检查用户凭据（异步）
                    userLoginRequest(username,password);
                }

            }
        });

        // 注册按钮点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)); // 跳转到注册界面
            }
        });

    }
    private void saveUserToSession(User user) {
        SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", user.getUsername());
        editor.putString("token", user.getToken()); // 如果有 token
        editor.apply();
    }
    private void userLoginRequest(String username, String password) {
        // 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();

        // 构建请求地址
        String url = "http://10.0.2.2:8081/api/login";

        // 构建 JSON 参数
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

        // 构建请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("OkHttp", "请求失败", e);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);

                        // 假设后端返回结构是：{ "username": "xxx", "token": "yyy" }
                        String username = jsonObject.getString("username");
                        String token = jsonObject.getString("token");

                        User user = new User(username, token);

                        // 保存用户信息到本地
                        saveUserToSession(user);

                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                            // 跳转到 MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 清除栈内其他 Activity
                            startActivity(intent);
                        });
                    } catch (JSONException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "JSON 解析错误", Toast.LENGTH_SHORT).show();
                            Log.e("OkHttp", "JSON 解析失败", e);
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "请求失败，状态码：" + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e("OkHttp", "请求失败，状态码: " + response.code());
                    });
                }
            }
        });
    }
}