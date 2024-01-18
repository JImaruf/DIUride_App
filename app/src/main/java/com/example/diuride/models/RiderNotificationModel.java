package com.example.diuride.models;

public class RiderNotificationModel {
    String Pname,Pdp,Puid;


    public RiderNotificationModel() {
    }

    public RiderNotificationModel(String pname, String pdp, String puid) {
        Pname = pname;
        Pdp = pdp;
        Puid = puid;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPdp() {
        return Pdp;
    }

    public void setPdp(String pdp) {
        Pdp = pdp;
    }

    public String getPuid() {
        return Puid;
    }

    public void setPuid(String puid) {
        Puid = puid;
    }
}
