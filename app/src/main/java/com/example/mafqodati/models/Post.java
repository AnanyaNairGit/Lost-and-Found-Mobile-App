package com.example.mafqodati.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.mafqodati.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;


public class Post {

    private static Post instance;
    private String title;
    private String content;
    private int type;
    private long creationDate;
    private List<String> imagesUri = new ArrayList<>();
    private String writerId;
    private double longitude;
    private double latitude;
    private boolean isFinished;
    private String city;
    private String district;

    private String categoryId;

    public Post(String title, String content, int type, long creationDate, ArrayList<String> imagesUri, String writerId, double longitude, double latitude, boolean isFinished, String city, String district, String categoryId) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.creationDate = creationDate;
        this.imagesUri = imagesUri;
        this.writerId = writerId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isFinished = isFinished;
        this.city = city;
        this.district = district;
        this.categoryId = categoryId;
    }

    private Post() {
    }

    public static Post getInstance() {
        if (instance == null) {
            instance = new Post();
            return instance;
        }
        return instance;
    }

    public static void destruct() {
        instance = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getImagesUri() {
        return imagesUri;
    }

    public void setImagesUri(List<String> imagesUri) {
        this.imagesUri = imagesUri;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", creationDate='" + creationDate + '\'' +
                ", imagesUri=" + imagesUri +
                ", writerId='" + writerId + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", isFinished=" + isFinished +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
