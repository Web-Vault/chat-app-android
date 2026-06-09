package com.example.newchatapp;

public class Message {

    private String messageId;
    private String message;
    private String senderId;
    private long timestamp;
    private boolean seen;
    private boolean delivered;
    private boolean deleted;
    private long deliveredTime;
    private long seenTime;
    public Message() {
        // Required empty constructor
    }

    public Message(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.seen = false;
        this.delivered = false;
        this.deleted = false;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(long deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public long getSeenTime() {
        return seenTime;
    }

    public void setSeenTime(long seenTime) {
        this.seenTime = seenTime;
    }
}