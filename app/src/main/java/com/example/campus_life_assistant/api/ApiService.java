package com.example.campus_life_assistant.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/chat")
    Call<com.example.campus_life_assistant.api.ChatResponse> sendMessage(@Body ChatRequest body);
}