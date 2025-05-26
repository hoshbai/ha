package com.example.AndroidServer.model;

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

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}