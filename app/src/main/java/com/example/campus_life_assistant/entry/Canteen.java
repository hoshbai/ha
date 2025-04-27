package com.example.campus_life_assistant.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Canteen implements Serializable {
    private String name;        // 食堂名称
    private String location;    // 位置
    private String openingHours; // 营业时间
    private int queueStatus;    // 排队状态（0-5，0表示无人，5表示非常拥挤）
    private List<Food> menu;    // 菜单
    private List<Food> recommendations; // 每日推荐

    public Canteen(String name, String location, String openingHours) {
        this.name = name;
        this.location = location;
        this.openingHours = openingHours;
        this.queueStatus = 0;
        this.menu = new ArrayList<>();
        this.recommendations = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public int getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(int queueStatus) {
        this.queueStatus = queueStatus;
    }

    public List<Food> getMenu() {
        return menu;
    }

    public void addFood(Food food) {
        this.menu.add(food);
    }

    public List<Food> getRecommendations() {
        return recommendations;
    }

    public void addRecommendation(Food food) {
        food.setRecommended(true);
        this.recommendations.add(food);
    }

    // 获取排队状态的文字描述
    public String getQueueStatusText() {
        switch (queueStatus) {
            case 0: return "无人排队";
            case 1: return "几乎无人";
            case 2: return "人数较少";
            case 3: return "一般人数";
            case 4: return "人数较多";
            case 5: return "非常拥挤";
            default: return "数据未知";
        }
    }
}