package com.example.campus_life_assistant.model;

public class Dormitory {
    private double balance;

    private String buildingNo;
    private String roomNo;
    private String createTime;
    private String username;

    public Dormitory() {
    }

    public Dormitory(String buildingNo, String roomNo, String username) {
        this.buildingNo = buildingNo;
        this.roomNo = roomNo;
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUsername() {
        return username;
    }
}