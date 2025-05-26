package com.example.campus_life_assistant.news.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "collected_news")
public class CollectedNews {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String newsId; // Assuming a unique ID for each news item
    private String title;
    private String publisher;
    private String publishTime;
    private String imageUrl;
    // Add other fields you want to store

    // Constructor
    public CollectedNews(String newsId, String title, String publisher, String publishTime, String imageUrl) {
        this.newsId = newsId;
        this.title = title;
        this.publisher = publisher;
        this.publishTime = publishTime;
        this.imageUrl = imageUrl;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
} 