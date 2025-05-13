package com.example.campus_life_assistant.entry;

import java.io.Serializable;
import java.util.Date;

/**
 * 失物招领物品实体类
 */
public class LostFoundItem implements Serializable {
    public static final int TYPE_LOST = 0; // 寻物启事
    public static final int TYPE_FOUND = 1; // 招领启事
    
    private int id;
    private String title; // 标题
    private String description; // 描述
    private String location; // 丢失/拾取地点
    private Date time; // 丢失/拾取时间
    private String category; // 物品类别：证件、电子、书本等
    private String contact; // 联系方式
    private String contactType; // 联系方式类型：电话、微信、QQ等
    private String imageUrl; // 图片URL
    private int itemType; // 类型：失物、招领
    private String publisherName; // 发布者姓名
    private String publisherId; // 发布者ID
    private Date publishTime; // 发布时间
    private boolean isCompleted; // 是否已完成（已找到/已归还）
    
    public LostFoundItem() {
    }
    
    public LostFoundItem(int id, String title, String description, String location, 
                         Date time, String category, String contact, String contactType, 
                         String imageUrl, int itemType, String publisherName, String publisherId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.time = time;
        this.category = category;
        this.contact = contact;
        this.contactType = contactType;
        this.imageUrl = imageUrl;
        this.itemType = itemType;
        this.publisherName = publisherName;
        this.publisherId = publisherId;
        this.publishTime = new Date();
        this.isCompleted = false;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    public String getItemTypeText() {
        return itemType == TYPE_LOST ? "寻物启事" : "招领启事";
    }
} 