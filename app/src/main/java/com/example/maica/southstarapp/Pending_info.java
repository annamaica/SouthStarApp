package com.example.maica.southstarapp;

/**
 * Created by Maica on 2/1/2018.
 */

public class Pending_info {

    String order_id;
    String order_user;
    String order_address;
    String order_medicine_code;
    String order_quantity;
    String order_date;
    String order_status;

    public Pending_info(String order_id, String order_user, String order_address, String order_medicine_code, String order_quantity, String order_date, String order_status) {
        this.order_id = order_id;
        this.order_user = order_user;
        this.order_address = order_address;
        this.order_medicine_code = order_medicine_code;
        this.order_quantity = order_quantity;
        this.order_date = order_date;
        this.order_status = order_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getOrder_user() {
        return order_user;
    }

    public String getOrder_address() { return order_address; }

    public String getOrder_medicine_code() {
        return order_medicine_code;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_status() {return order_status; }
    public Pending_info(){

    }
}
