package com.example.maica.southstarapp;

/**
 * Created by Maica on 2/15/2018.
 */

public class Sales {
    String med_id;
    String med_code;
    String med_name;
    String med_quantity;
    String med_status;
    String med_sales;

    public Sales(String med_id, String med_code, String med_name, String med_quantity, String med_status, String med_sales) {
        this.med_id = med_id;
        this.med_code = med_code;
        this.med_name = med_name;
        this.med_quantity = med_quantity;
        this.med_status = med_status;
        this.med_sales = med_sales;
    }

    public String getMed_id() {
        return med_id;
    }

    public String getMed_code(){
        return med_code;
    }

    public String getMed_name() {
        return med_name;
    }

    public String getMed_quantity() {
        return med_quantity;
    }

    public String getMed_status() { return  med_status; }

    public String getMed_sales() { return med_sales; }

    public Sales(){

    }
}
