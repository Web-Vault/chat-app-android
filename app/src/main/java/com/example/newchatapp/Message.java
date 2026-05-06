package com.example.newchatapp;

public class Message {

    private String message;
    private String senderId;
    private long timestamp;
    private boolean seen;
    private boolean delivered;

    public Message() {
        // Required empty constructor
    }

    public Message(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.seen = false;
        this.delivered = false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}