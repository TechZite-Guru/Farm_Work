package com.example.farmwork;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class WorkerViewModel extends ViewModel implements Serializable {

    private String profile_image;
    private String name;
    private String location;
    private String phone;
    private String uId;
    private String email;
    private String postalcode;

    public WorkerViewModel(String postalcode) {
        this.postalcode = postalcode;
    }

    public WorkerViewModel(String email, String name, String phone, String location, String profile_image, String uid){
        this.profile_image = profile_image;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.uId = uid;
        this.email = email;
    }

    public String getProfile_image(){
        return profile_image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() { return location; }

    public String getuId() { return uId; }

    public String getPostalcode() { return postalcode; }
}