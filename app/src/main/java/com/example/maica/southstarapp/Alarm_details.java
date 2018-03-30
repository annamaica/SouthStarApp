package com.example.maica.southstarapp;

/**
 * Created by Maica on 3/5/2018.
 */

public class Alarm_details {

    String alarm_id;
    String alarm_user;
    String alarm_title;
    String alarm_quantity;
    String alarm_desc;
    Integer alarm_hour;
    Integer alarm_minute;

    public Alarm_details(String alarm_id, String alarm_user, String alarm_title, String alarm_quantity, String alarm_desc, Integer alarm_hour, Integer alarm_minute) {
        this.alarm_id = alarm_id;
        this.alarm_user = alarm_user;
        this.alarm_title = alarm_title;
        this.alarm_quantity = alarm_quantity;
        this.alarm_desc = alarm_desc;
        this.alarm_hour = alarm_hour;
        this.alarm_minute = alarm_minute;
    }

    public String getAlarm_id() { return  alarm_id; }

    public String getAlarm_user() {
        return alarm_user;
    }

    public String getAlarm_title() {
        return alarm_title;
    }

    public String getAlarm_quantity() {
        return alarm_quantity;
    }

    public String getAlarm_desc() {
        return alarm_desc;
    }

    public Integer getAlarm_hour() {return alarm_hour;}

    public Integer getAlarm_minute() {return alarm_minute; }

    public Alarm_details(){

    }
}
