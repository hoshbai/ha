package com.example.campus_life_assistant.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Food implements Serializable {
    private String name;        // 菜品名称
    private String description; // 菜品描述
    private double price;       // 价格
    private String category;    // 类别（如：主食、荤菜、素菜等）
    private float rating;       // 平均评分
    private int ratingCount;    // 评分数量
    private boolean isRecommended; // 是否推荐
    private String imageUrl;    // 图片URL
    private List<FoodComment> comments; // 评论

    public Food(String name, String description, double price, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.rating = 0;
        this.ratingCount = 0;
        this.isRecommended = false;
        this.comments = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<FoodComment> getComments() {
        return comments;
    }

    public void addComment(FoodComment comment) {
        this.comments.add(comment);
    }

    // 添加新评分方法
    public void addRating(float newRating) {
        float totalRating = this.rating * this.ratingCount + newRating;
        this.ratingCount++;
        this.rating = totalRating / this.ratingCount;
    }
}