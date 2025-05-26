package com.example.AndroidServer.model;

public class ChargeResponse {
    private boolean success;
    private String message;
    private double balance;
    private ChargeHistory history; // 当前这条充值记录
    public ChargeResponse() {
    }

    public ChargeResponse(boolean success, String message, double balance) {
        this.success = success;
        this.message = message;
        this.balance = balance;
    }

    public ChargeResponse(boolean success, String message, double balance, ChargeHistory history) {
        this.success = success;
        this.message = message;
        this.balance = balance;
        this.history = history;
    }

    public ChargeHistory getHistory() {
        return history;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getBalance() { return balance; }
}