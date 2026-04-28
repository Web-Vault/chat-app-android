package com.example.newchatapp;

public class Message {

    String message;
    String sender;

    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}