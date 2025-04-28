package com.example.campus_life_assistant.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String BASE_URL = " http://10.0.2.2:3000/"; // 替换为你的后端 URL

    private static Retrofit retrofit;

    public static ApiService getApiService() {
        // 创建 OkHttpClient 并设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 设置连接超时时间为 60 秒
                .readTimeout(60, TimeUnit.SECONDS)    // 设置读取超时时间为 60 秒
                .writeTimeout(60, TimeUnit.SECONDS)   // 设置写入超时时间为 60 秒
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}