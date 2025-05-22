package com.example.campus_life_assistant.model;

import com.google.gson.annotations.SerializedName;

public class LibraryNotification {
    @SerializedName("id")
    private int id;

    @SerializedName("message")
    private String message;

    @SerializedName("date")
    private String date;

    // —— getters & setters —— //

    public int getId() { return id; }
    public String getMessage() { return message; }
    public String getDate() { return date; }

    public void setMessage(String message) { this.message = message; }
    public void setDate(String date) { this.date = date; }
}
