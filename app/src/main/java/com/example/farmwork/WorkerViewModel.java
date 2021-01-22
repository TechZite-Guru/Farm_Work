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


    public WorkerViewModel(String email, String name, String phone, String location, String profile_image){
        this.profile_image = profile_image;
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    public String getProfile_image(){
        return profile_image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() { return location; }
}