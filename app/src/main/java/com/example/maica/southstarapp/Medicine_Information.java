package com.example.maica.southstarapp;

/**
 * Created by Maica on 1/15/2018.
 */

public class Medicine_Information {

    String medicine_id;
    String medicine_code;
    String medicine_name;
    String medicine_price;
    String medicine_desc;
    String medicine_stock;
    String medicine_category;
    String medicine_dosage;
    String medicine_type;
    String medicine_image;
    String url;
    String medicine_status;

    public Medicine_Information(String medicine_id, String medicine_code, String medicine_name, String medicine_price, String medicine_desc, String medicine_stock, String medicine_category, String medicine_dosage, String medicine_type, String medicine_image, String url, String medicine_status) {
        this.medicine_id = medicine_id;
        this.medicine_code = medicine_code;
        this.medicine_name = medicine_name;
        this.medicine_price = medicine_price;
        this.medicine_desc = medicine_desc;
        this.medicine_stock = medicine_stock;
        this.medicine_category = medicine_category;
        this.medicine_dosage = medicine_dosage;
        this.medicine_type = medicine_type;
        this.medicine_image = medicine_image;
        this.url = url;
        this.medicine_status = medicine_status;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public String getMedicine_code() {
        return medicine_code;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getMedicine_price() {
        return medicine_price;
    }

    public String getMedicine_desc() {
        return medicine_desc;
    }

    public String getMedicine_stock(){ return medicine_stock; }

    public String getMedicine_category() {return medicine_category; }

    public String getMedicine_dosage() { return medicine_dosage; }

    public String getMedicine_type() { return medicine_type; }

    public String getMedicine_image(){ return medicine_image; }

    public String getUrl(){ return  url; }

    public String getMedicine_status() {return  medicine_status; }

    public Medicine_Information () { }
}
