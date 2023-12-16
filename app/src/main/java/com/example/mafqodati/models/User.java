package com.example.mafqodati.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class User {

    private String userId;
    private String userEmail;
    private String userNickName;
    private String userProfileImgURL;
    private boolean isAdmin;
    private Timestamp registerTime;
    private String fcmToken;

    public User() { }

    public User(String userEmail, String userNickName, String userProfileImgURL, Timestamp registerTime, String fcmToken) {
        this.userEmail = userEmail;
        this.userNickName = userNickName;
        this.userProfileImgURL = userProfileImgURL;
        this.fcmToken = fcmToken;
        this.isAdmin = false;
        this.registerTime =  registerTime;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfileImgURL() {
        return userProfileImgURL;
    }

    public void setUserProfileImgURL(String userProfileImgURL) {
        this.userProfileImgURL = userProfileImgURL;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
