package com.example.mafqodati.models;

import com.google.firebase.Timestamp;

public class ChatData {


    private String userId;

    private String message;

    private Timestamp date;

    public ChatData() {}

    public ChatData(String userId, String message, Timestamp date) {
        this.userId = userId;
        this.message = message;
        this.date = date;
    }

    public ChatData(String userId, String message){
        this.userId = userId;
        this.message = message;
    }

    public String getUserId(){ return userId; }
    public String getMessage(){ return message; }
    public Timestamp getDate() { return date; }
}
