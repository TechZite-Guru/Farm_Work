package com.example.farmwork;

import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class DatesViewModel extends ViewModel implements Serializable {
    String date;
    String id2;

    DatesViewModel(String date, String id) {
        this.date = date;
        this.id2 = id;
    }

    public String getDate() { return date; }

    public String getId2() { return id2; }

}
