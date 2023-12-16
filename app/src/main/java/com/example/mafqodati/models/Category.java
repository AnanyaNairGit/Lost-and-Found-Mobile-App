package com.example.mafqodati.models;

public class Category {
    private String imageUri ;
    private String nameEn ;
    private String nameAr ;
    private String code ;

    public Category() {
    }

    public Category(String imageUri, String nameEn, String nameAr, String code) {
        this.imageUri = imageUri;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.code = code;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
