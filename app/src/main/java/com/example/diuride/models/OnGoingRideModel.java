package com.example.diuride.models;

public class OnGoingRideModel {

    String Ruid;
    String Puid;
    String destination;
    String startingpoint;
    String rPrice;
    String rTime;


    public OnGoingRideModel() {
    }

    public OnGoingRideModel(String ruid, String puid, String destination, String startingpoint, String rPrice, String rTime) {
        Ruid = ruid;
        Puid = puid;
        this.destination = destination;
        this.startingpoint = startingpoint;
        this.rPrice = rPrice;
        this.rTime = rTime;
    }

    public String getRuid() {
        return Ruid;
    }

    public void setRuid(String ruid) {
        Ruid = ruid;
    }

    public String getPuid() {
        return Puid;
    }

    public void setPuid(String puid) {
        Puid = puid;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartingpoint() {
        return startingpoint;
    }

    public void setStartingpoint(String startingpoint) {
        this.startingpoint = startingpoint;
    }

    public String getrPrice() {
        return rPrice;
    }

    public void setrPrice(String rPrice) {
        this.rPrice = rPrice;
    }

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }
}
