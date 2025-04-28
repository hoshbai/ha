package com.example.campus_life_assistant.entry;

public class Message {
    private String content;
    private boolean isUserMessage; // 是否是用户发送的消息

    public Message(String content, boolean isUserMessage) {
        this.content = content;
        this.isUserMessage = isUserMessage;
    }

    public String getContent() {
        return content;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }
}