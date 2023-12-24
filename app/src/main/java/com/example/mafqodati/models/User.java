package com.example.mafqodati.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class User {

    private static User instance ;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userProfileImgURL;
    private String userPhone;
    private boolean isAdmin;
    private long registerTime;
    private String fcmToken;

    private User() { }
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
            return instance;
        }
        return instance;
    }

    public static void destruct(){
        instance = null;
    }
    public User( String userEmail, String userFirstName, String userLastName, String userProfileImgURL, String userPhone, boolean isAdmin, long registerTime, String fcmToken) {
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userProfileImgURL = userProfileImgURL;
        this.userPhone = userPhone;
        this.isAdmin = isAdmin;
        this.registerTime = registerTime;
        this.fcmToken = fcmToken;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }



    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserProfileImgURL() {
        return userProfileImgURL;
    }

    public void setUserProfileImgURL(String userProfileImgURL) {
        this.userProfileImgURL = userProfileImgURL;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
