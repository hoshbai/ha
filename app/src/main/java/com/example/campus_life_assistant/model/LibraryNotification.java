package com.example.campus_life_assistant.model;

public class LibraryNotification {
    public static final int TYPE_DUE_SOON = 1;
    public static final int TYPE_OVERDUE = 2;
    public static final int TYPE_ANNOUNCEMENT = 3;
    public static final int TYPE_RECOMMENDATION = 4;

    private int type;
    private String title;
    private String content;
    private long timestamp;

    public LibraryNotification(int type, String title, String content, long timestamp) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}