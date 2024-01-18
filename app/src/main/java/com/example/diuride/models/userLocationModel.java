package com.example.diuride.models;

public class userLocationModel {

    String lat;
    String lon;
    String uName;
    String uID;

    public userLocationModel() {
    }

    public userLocationModel(String lat, String lon, String uName, String uID) {
        this.lat = lat;
        this.lon = lon;
        this.uName = uName;
        this.uID = uID;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}


