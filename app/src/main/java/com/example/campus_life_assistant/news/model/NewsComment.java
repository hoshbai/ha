package com.example.campus_life_assistant.news.model;

public class NewsComment {
    private String id;
    private String newsId;
    private String commenterName;
    private String commentTime;
    private String content;
    private int likes;

    // Constructor
    public NewsComment(String id, String newsId, String commenterName, String commentTime, String content, int likes) {
        this.id = id;
        this.newsId = newsId;
        this.commenterName = commenterName;
        this.commentTime = commentTime;
        this.content = content;
        this.likes = likes;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNewsId() {
        return newsId;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public String getContent() {
        return content;
    }

    public int getLikes() {
        return likes;
    }

    // Setters (optional, add if needed)
    public void setId(String id) {
        this.id = id;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
} 