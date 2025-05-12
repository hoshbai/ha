package com.example.campus_life_assistant.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.campus_life_assistant.api.ApiService;
import com.example.campus_life_assistant.api.ChatRequest;
import com.example.campus_life_assistant.api.ChatResponse;
import com.example.campus_life_assistant.api.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<String> thinkResponse = new MutableLiveData<>(); // 存储 <think> 部分
    private MutableLiveData<String> finalResponse = new MutableLiveData<>(); // 存储正式回答部分

    public LiveData<String> getThinkResponse() {
        return thinkResponse;
    }

    public LiveData<String> getFinalResponse() {
        return finalResponse;
    }

    public void sendMessage(String message) {
        ApiService apiService = RetrofitInstance.getApiService(); // 获取 API 服务实例
        apiService.sendMessage(new ChatRequest(message)) // 发送消息
                .enqueue(new Callback<ChatResponse>() { // 异步处理响应
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ChatResponse chatResponse = response.body();
                            String serverResponse = chatResponse.getContent();
                            Log.d("ServerResponse", "Parsed: " + serverResponse);

                            // 更新 LiveData
                            thinkResponse.setValue(extractThinkPart(serverResponse));
                            finalResponse.setValue(extractFinalPart(serverResponse));
                        } else {
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                                Log.e("ServerResponse", "Error Code: " + response.code() + ", Error Body: " + errorBody);
                                finalResponse.setValue("Error: " + errorBody);
                            } catch (Exception e) {
                                Log.e("ServerResponse", "Error parsing error body: " + e.getMessage());
                                finalResponse.setValue("Error: Failed to parse response");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {
                        Log.e("ServerResponse", "Network Failure: " + t.getMessage());
                        finalResponse.setValue("Error: " + t.getMessage());
                    }
                });
    }
    // 提取 <think> 部分
    private String extractThinkPart(String serverResponse) {
        int startIndex = serverResponse.indexOf("<think>");
        int endIndex = serverResponse.indexOf("</think>");
        if (startIndex != -1 && endIndex != -1) {
            return serverResponse.substring(startIndex + 7, endIndex).trim(); // 去掉 <think> 标签
        }
        return ""; // 如果没有 <think> 部分，返回空字符串
    }

    // 提取正式回答部分
    private String extractFinalPart(String serverResponse) {
        int endIndex = serverResponse.indexOf("</think>");
        if (endIndex != -1) {
            return serverResponse.substring(endIndex + 8).trim(); // 去掉 </think> 标签及之前的内容
        }
        return serverResponse.trim(); // 如果没有 <think> 部分，直接返回完整内容
    }
}