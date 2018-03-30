package com.example.maica.southstarapp;

/**
 * Created by Maica on 1/31/2018.
 */

public class Cart_info {
    String order_id;
    String order_medicine_id;
    String order_medicine_quantity;
    String order_total;

    public Cart_info(String order_id, String order_medicine_id, String order_medicine_quantity, String order_total) {
        this.order_id = order_id;
        this.order_medicine_id = order_medicine_id;
        this.order_medicine_quantity = order_medicine_quantity;
        this.order_total = order_total;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getOrder_medicine_id() {
        return order_medicine_id;
    }

    public String getOrder_medicine_quantity(){
        return order_medicine_quantity;
    }

    public String getOrder_total() { return order_total; }


    public Cart_info(){

    }
}
