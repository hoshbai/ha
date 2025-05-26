package com.example.campus_life_assistant.model;

public class ChargeRequest {
    private String buildingNo;
    private String roomNo;
    private double amount;
    private String name;

    public ChargeRequest(String buildingNo, String roomNo, double amount, String name) {
        this.buildingNo = buildingNo;
        this.roomNo = roomNo;
        this.amount = amount;
        this.name = name;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}