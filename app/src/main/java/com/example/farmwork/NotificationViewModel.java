package com.example.farmwork;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationViewModel extends ViewModel {
    String notifi;

    NotificationViewModel(String notifi) {
        this.notifi = notifi;
    }

    public String getNotifi() { return notifi; }
}