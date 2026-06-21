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

    // ===========================
    // Reply Message Fields
    // ===========================

    private boolean reply;

    private String replyMessageId;
    private String replyMessage;
    private String replySenderId;
    private String replySenderName;

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

        this.reply = false;
    }

    // ===========================
    // Basic Message
    // ===========================

    public String getMessage() {
        return message;
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

    // ===========================
    // Status
    // ===========================

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isDelivered() {
        return delivered;
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

    // ===========================
    // Reply
    // ===========================

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public String getReplyMessageId() {
        return replyMessageId;
    }

    public void setReplyMessageId(String replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public String getReplySenderId() {
        return replySenderId;
    }

    public void setReplySenderId(String replySenderId) {
        this.replySenderId = replySenderId;
    }

    public String getReplySenderName() {
        return replySenderName;
    }

    public void setReplySenderName(String replySenderName) {
        this.replySenderName = replySenderName;
    }
}