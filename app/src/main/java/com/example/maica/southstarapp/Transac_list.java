package com.example.maica.southstarapp;

/**
 * Created by Maica on 2/3/2018.
 */

public class Transac_list {
    String transac_id;
    String transac_user;
    String transac_address;
    String transac_medicine_code;
    String transac_quantity;
    String transac_date;
    String transac_status;

    public Transac_list(String transac_id, String transac_user, String transac_address, String transac_medicine_code, String transac_quantity, String transac_date, String transac_status) {
        this.transac_id = transac_id;
        this.transac_user = transac_user;
        this.transac_address = transac_address;
        this.transac_medicine_code = transac_medicine_code;
        this.transac_quantity = transac_quantity;
        this.transac_date = transac_date;
        this.transac_status = transac_status;
    }

    public String getTransac_id() {
        return transac_id;
    }

    public String getTransac_user() {
        return transac_user;
    }

    public String getTransac_address() {
        return transac_address;
    }

    public String getTransac_medicine_code() {
        return transac_medicine_code;
    }

    public String getTransac_quantity() {
        return transac_quantity;
    }

    public String getTransac_date() {
        return transac_date;
    }

    public String getTransac_status() {
        return transac_status;
    }

    public Transac_list(){

    }
}
