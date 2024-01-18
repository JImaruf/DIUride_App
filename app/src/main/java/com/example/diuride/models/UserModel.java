package com.example.diuride.models;

public class UserModel {

    String email, name, diuid, proimage, userType, FCMtoken;

    public UserModel() {

    }

    public UserModel(String email, String name, String diuid, String proimage, String userType, String FCMtoken) {
        this.email = email;
        this.name = name;
        this.diuid = diuid;
        this.proimage = proimage;
        this.userType = userType;
        this.FCMtoken = FCMtoken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiuid() {
        return diuid;
    }

    public void setDiuid(String diuid) {
        this.diuid = diuid;
    }

    public String getProimage() {
        return proimage;
    }

    public void setProimage(String proimage) {
        this.proimage = proimage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFCMtoken() {
        return FCMtoken;
    }

    public void setFCMtoken(String FCMtoken) {
        this.FCMtoken = FCMtoken;
    }
}