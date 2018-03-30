package com.example.maica.southstarapp;

/**
 * Created by Maica on 1/17/2018.
 */

public class MedicineList {

    public String medicine_name;
    public String medicine_code;
    public String medicine_price;
    public String medicine_id;

    public MedicineList(String medicine_name, String medicine_code, String medicine_price, String medicine_id) {
        this.medicine_name = medicine_name;
        this.medicine_code = medicine_code;
        this.medicine_price = medicine_price;
        this.medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getMedicine_code() {
        return medicine_code;
    }

    public String getMedicine_price() {
        return medicine_price;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public MedicineList(){

    }
}
