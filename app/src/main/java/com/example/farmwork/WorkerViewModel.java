package com.example.farmwork;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class WorkerViewModel extends ViewModel implements Serializable {

    private String profile_image;
    private String name;
    private String adminarea;
    private String locality;
    private String phone;
    private String uId;
    private String email;
    private String postalcode;
    private String fare;
    private double latitude, longitude;
    private double mylatitude, mylongitude;

    public WorkerViewModel(String postalcode) {
        this.postalcode = postalcode;
    }

    public WorkerViewModel(String email, String name, String phone, String adminarea, String locality, String profile_image, double latitude, double longitude, String fare, String uid, double mylatitude, double mylongitude){
        this.profile_image = profile_image;
        this.name = name;
        this.adminarea = adminarea;
        this.locality = locality;
        this.phone = phone;
        this.uId = uid;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fare = fare;
        this.mylatitude = mylatitude;
        this.mylongitude = mylongitude;
    }

    public String getProfile_image(){
        return profile_image;
    }

    public String getName() {
        return name;
    }

    public String getAdminarea() { return adminarea; }

    public String getLocality() { return locality; }

    public String getuId() { return uId; }

    public String getFare() { return fare; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public double getMylatitude() { return mylatitude; }

    public double getMylongitude() { return mylongitude; }

    public String getPostalcode() { return postalcode; }
}