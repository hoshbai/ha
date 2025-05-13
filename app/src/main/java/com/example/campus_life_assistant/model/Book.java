package com.example.campus_life_assistant.model;

public class Book {
    private String title;
    private String author;
    private String category;
    private String imageUrl;
    private float rating;
    private boolean isBorrowed;
    private boolean isWishlisted;
    private String status; // "想读", "在读", "已读"

    public Book(String title, String author, String category, String imageUrl, float rating) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isBorrowed = false;
        this.isWishlisted = false;
        this.status = "";
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}