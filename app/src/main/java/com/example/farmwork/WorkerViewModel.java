package com.example.farmwork;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkerViewModel extends ViewModel {

    private String profile_image;
    private String name;
    private String location;
    private int category_row;

    WorkerViewModel(){

    }

    public WorkerViewModel(String profile_image, String name, String location){
        this.profile_image = profile_image;
        this.name = name;
        this.location = location;
    }

    public String getProfile_image(){
        return profile_image;
    }

    public String getName() {
        return name;
    }

    public String getLocation(){ return location; }

    public int getCategory_row() { return category_row; };
}