package com.example.campus_life_assistant.entry;

public class Message {
    private String content;
    private boolean isUserMessage; // 是否是用户消息
    private boolean isThink;       // 是否是思考部分

    public Message(String content, boolean isUserMessage, boolean isThink) {
        this.content = content;
        this.isUserMessage = isUserMessage;
        this.isThink = isThink;
    }

    public String getContent() {
        return content;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }

    public boolean isThink() {
        return isThink;
    }
}