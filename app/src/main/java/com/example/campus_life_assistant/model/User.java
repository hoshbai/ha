package com.example.campus_life_assistant.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String nickname;
    public String avatar;
    public String gender;
    public String email;

    public User() { }  // DataSnapshot.getValue(User.class) 需要

    public User(String nickname, String avatar, String gender, String email) {
        this.nickname = nickname;
        this.avatar   = avatar;
        this.gender   = gender;
        this.email    = email;
    }
}
