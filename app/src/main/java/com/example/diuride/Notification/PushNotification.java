package com.example.diuride.Notification;

public class PushNotification {
    NotificationData data;
    String to = "";

    public PushNotification(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }
}
