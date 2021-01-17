package com.example.farmwork;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkerViewModel extends ViewModel {

    private int prefix;
    private String category;
    private int category_row;

    public WorkerViewModel(int prefix, String category){
        this.prefix = prefix;
        this.category = category;
    }

    public int getPrefix(){
        return prefix;
    }

    public String getCategory() {
        return category;
    }

    public int getCategory_row() { return category_row; };
}