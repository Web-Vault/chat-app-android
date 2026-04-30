package com.example.newchatapp;

public class ChatUser {

    private String uid;
    private String name;
    private String mobileNumber;
    private String lastMessage;
    private String time;
    private long lastMessageTime;

    public ChatUser() {
        // Required empty constructor
    }

    public ChatUser(String uid, String name, String mobileNumber,
                    String lastMessage, String time,
                    long lastMessageTime) {
        this.uid = uid;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.lastMessage = lastMessage;
        this.time = time;
        this.lastMessageTime = lastMessageTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}