package com.example.campus_life_assistant.news.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "collected_news")
public class NewsItem implements Serializable {
    @PrimaryKey
    private String title;
    private String publisher;
    private String publishTime;
    private String imageUrl;
    private int likes;
    private int views;
    private String content;

    // Constructor
    public NewsItem(String title, String publisher, String publishTime, String imageUrl, int likes, int views, String content) {
        this.title = title;
        this.publisher = publisher;
        this.publishTime = publishTime;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.views = views;
        this.content = content;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public String getContent() {
        return content;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setContent(String content) {
        this.content = content;
    }
} 