package com.example.campus_life_assistant.campuscard.model;

public class ConsumptionRecord {
    private String description;
    private String time;
    private double amount;

    public ConsumptionRecord(String description, String time, double amount) {
        this.description = description;
        this.time = time;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public double getAmount() {
        return amount;
    }
} 