package com.example.campus_life_assistant.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.campus_life_assistant.api.ApiService;
import com.example.campus_life_assistant.api.ChatResponse;
import com.example.campus_life_assistant.api.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<String> response = new MutableLiveData<>();

    public LiveData<String> getResponse() {
        return response;
    }

    public void sendMessage(String message) {
        ApiService apiService = RetrofitInstance.getApiService();
        apiService.sendMessage(new com.example.campus_life_assistant.api.ChatRequest(message))
            .enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ChatResponse chatResponse = response.body();
                        String serverResponse = chatResponse.getContent(); // 获取服务器返回的字符串
                        Log.d("ServerResponse", "Parsed: " + serverResponse);

                        // 更新 LiveData，以便在 UI 中显示
                        ChatViewModel.this.response.setValue(serverResponse);
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("ServerResponse", "Error Code: " + response.code() + ", Error Body: " + errorBody);
                            ChatViewModel.this.response.setValue("Error: " + errorBody);
                        } catch (Exception e) {
                            Log.e("ServerResponse", "Error parsing error body: " + e.getMessage());
                            ChatViewModel.this.response.setValue("Error: Failed to parse response");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {
                    Log.e("ServerResponse", "Network Failure: " + t.getMessage());
                    ChatViewModel.this.response.setValue("Error: " + t.getMessage());
                }
            });
    }
}