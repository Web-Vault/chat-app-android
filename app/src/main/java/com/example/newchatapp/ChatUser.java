package com.example.newchatapp;

public class ChatUser {

    String name;
    String lastMessage;
    String time;

    public ChatUser(String name, String lastMessage, String time) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }
}