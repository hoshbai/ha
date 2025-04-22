package com.example.campus_life_assistant.entry;

public class Announcement {
    private String title;
    private String author;
    private String date;
    private boolean pinned;
    private String content;

    public Announcement(String title, String author, String date, boolean pinned, String content) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.pinned = pinned;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public String getContent() {
        return content;
    }
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}