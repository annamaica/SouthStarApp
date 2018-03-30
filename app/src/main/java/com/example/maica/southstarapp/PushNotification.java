package com.example.maica.southstarapp;

/**
 * Created by Maica on 2/8/2018.
 */

public class PushNotification {

    public String notifID_Order;
    public String notifUser;
    public String notifComment;

    public PushNotification(String notifID_Order, String notifUser, String notifComment) {
        this.notifID_Order = notifID_Order;
        this.notifUser = notifUser;
        this.notifComment = notifComment;
    }

    public String getNotifID_Order() {
        return notifID_Order;
    }

    public String getNotifUser() {
        return notifUser;
    }

    public String getNotifComment() {
        return notifComment;
    }

    public PushNotification(){

    }
}
