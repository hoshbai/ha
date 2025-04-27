package com.example.campus_life_assistant.entry;

import java.io.Serializable;
import java.util.Date;

public class FoodComment implements Serializable {
    private String userNickname; // 用户昵称
    private float rating;        // 用户评分
    private String comment;      // 评论内容
    private Date commentDate;    // 评论日期

    public FoodComment(String userNickname, float rating, String comment) {
        this.userNickname = userNickname;
        this.rating = rating;
        this.comment = comment;
        this.commentDate = new Date();
    }

    // Getters and Setters
    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}