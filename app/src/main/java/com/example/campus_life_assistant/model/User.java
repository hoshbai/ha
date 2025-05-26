package com.example.campus_life_assistant.model;

public class User {
    private String username;
    private String token; // 或其他字段

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
