package com.example.maica.southstarapp;

/**
 * Created by Maica on 1/19/2018.
 */

public class UserObject {
    public String userID;
    public String first_name;
    public String last_name;
    public String middle_name;
    public String user_type;

    public String address;

    public String age;
    public String birth_date;

    public UserObject(){

    }

    public UserObject(String userID, String first_name, String last_name, String middle_name, String user_type, String address, String age, String birth_date) {
        this.userID = userID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.user_type = user_type;
        this.address = address;
        this.age = age;
        this.birth_date = birth_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getUser_type() { return user_type; }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {

        return userID;
    }
}
