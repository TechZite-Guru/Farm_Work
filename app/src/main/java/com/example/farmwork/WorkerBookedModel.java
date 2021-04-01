package com.example.farmwork;

import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class WorkerBookedModel extends ViewModel implements Serializable {
    String boo_name, boo_phone, boo_village, boo_date;
    String name;
    String phone;
    String village;
    String date;

    WorkerBookedModel(String boo_name, String name, String boo_phone, String phone, String boo_village, String village, String boo_date, String date) {
        this.boo_name = boo_name;
        this.name = name;
        this.boo_phone = boo_phone;
        this.phone = phone;
        this.boo_village = boo_village;
        this.village = village;
        this.boo_date = boo_date;
        this.date = date;
    }

    public String getBoo_name() { return boo_name; }

    public String getName() { return name; }

    public String getBoo_phone() { return boo_phone; }

    public String getPhone() { return phone; }

    public String getBoo_village() { return boo_village; }

    public String getVillage() { return village; }

    public String getBoo_date() { return boo_date; }

    public String getDate() { return date; }
}
