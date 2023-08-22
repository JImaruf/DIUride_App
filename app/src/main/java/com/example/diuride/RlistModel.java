package com.example.diuride;

public class RlistModel {

    String Rname;
    String destination;
    int rDP;
    String rPrice;
    String rTime;


    public RlistModel(String rname, String destination, int rDP, String rPrice, String rTime) {
        Rname = rname;
        this.destination = destination;
        this.rDP = rDP;
        this.rPrice = rPrice;
        this.rTime = rTime;
    }

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }

    public String getRname() {
        return Rname;
    }

    public void setRname(String rname) {
        Rname = rname;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getrDP() {
        return rDP;
    }

    public void setrDP(int rDP) {
        this.rDP = rDP;
    }

    public String getrPrice() {
        return rPrice;
    }

    public void setrPrice(String rPrice) {
        this.rPrice = rPrice;
    }
}
