package com.example.campus_life_assistant.api;


public class ChatResponse {
    private String response; // 匹配服务器返回的字段名

    public String getContent() {
        return response; // 获取服务器返回的字符串
    }

    public void setContent(String response) {
        this.response = response;
    }
}