package com.example.diuride.models;

public class RlistModel {

    String Rname,Ruid;
    String destination;
    String startingpoint;
    String rDP;
    String rPrice;
    String rTime;
    String status="unbooked";
    String uType;


    public RlistModel() {
    }

    public RlistModel(String rname, String ruid, String destination, String startingpoint, String rDP, String rPrice, String rTime, String status, String uType) {
        Rname = rname;
        Ruid = ruid;
        this.destination = destination;
        this.startingpoint = startingpoint;
        this.rDP = rDP;
        this.rPrice = rPrice;
        this.rTime = rTime;
        this.status = status;
        this.uType = uType;
    }

    public String getRname() {
        return Rname;
    }

    public void setRname(String rname) {
        Rname = rname;
    }

    public String getRuid() {
        return Ruid;
    }

    public void setRuid(String ruid) {
        Ruid = ruid;
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

    public String getrDP() {
        return rDP;
    }

    public void setrDP(String rDP) {
        this.rDP = rDP;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }
}
