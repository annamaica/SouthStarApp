package com.example.maica.southstarapp;

/**
 * Created by Maica on 2/3/2018.
 */

public class PrescriptionAdapter {
    String prescription_id;
    String prescription_userid;
    String prescription_user;
    String prescription_image;
    String prescription_status;
    String url;

    public PrescriptionAdapter(String prescription_id, String prescription_userid, String prescription_user, String prescription_image, String prescription_status, String url) {
        this.prescription_id = prescription_id;
        this.prescription_userid = prescription_userid;
        this.prescription_user = prescription_user;
        this.prescription_image = prescription_image;
        this.prescription_status = prescription_status;
        this.url = url;
    }

    public String getPrescription_id() { return  prescription_id; }

    public String getPrescription_userid() {return  prescription_userid; }

    public String getPrescription_user() { return  prescription_user; }

    public String getPrescription_image() {
        return prescription_image;
    }

    public String getPrescription_status() { return  prescription_status; }

    public String getUrl() {
        return url;
    }

    public PrescriptionAdapter(){

    }
}

