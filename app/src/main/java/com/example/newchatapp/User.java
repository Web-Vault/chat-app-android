package com.example.newchatapp;

public class User {

    private String uid;
    private String name;
    private String mobileNumber;
    private String profileImage;
    private String status;
    private long lastSeen;
    private boolean isOnline;

    // Empty constructor required for Firebase
    public User() {
    }

    // Full constructor
    public User(String uid, String name, String mobileNumber,
                String profileImage, String status, long lastSeen, boolean isOnline) {

        this.uid = uid;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.profileImage = profileImage;
        this.status = status;
        this.lastSeen = lastSeen;
        this.isOnline = isOnline;
    }

    // Getters and Setters

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}