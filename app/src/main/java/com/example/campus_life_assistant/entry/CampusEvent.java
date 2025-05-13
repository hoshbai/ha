package com.example.campus_life_assistant.entry;

import java.io.Serializable;
import java.util.Date;

public class CampusEvent implements Serializable {
    private int id;
    private String title;
    private String description;
    private String location;
    private Date startTime;
    private Date endTime;
    private String organizer;
    private String imageUrl;
    private String category; // 活动类别：讲座、竞赛、文娱、体育等
    private boolean isRegistered; // 用户是否已报名此活动

    public CampusEvent() {
    }

    public CampusEvent(int id, String title, String description, String location, Date startTime, Date endTime, String organizer, String imageUrl, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.organizer = organizer;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isRegistered = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
} 