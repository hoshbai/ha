package com.example.campus_life_assistant.model;

public class ChargeResponse {
    private boolean success;
    private String message;
    private double balance;
    private ChargeHistory history; // 当前这条充值记录

    public ChargeHistory getHistory() {
        return history;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public double getBalance() {
        return balance;
    }
}