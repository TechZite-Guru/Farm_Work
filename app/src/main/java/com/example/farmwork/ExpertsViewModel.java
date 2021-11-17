package com.example.farmwork;

import java.io.Serializable;

public class ExpertsViewModel implements Serializable {

    String expertName, expertRole, availability, phoneNumber, language1, language2, language3;

    ExpertsViewModel(String expertName, String expertRole, String availability, String phoneNumber, String language1, String language2, String language3) {
        this.expertName = expertName;
        this.expertRole = expertRole;
        this.availability = availability;
        this.phoneNumber = phoneNumber;
        this.language1 = language1;
        this.language2 = language2;
        this.language3 = language3;
    }

    public String getExpertName() { return expertName; }

    public String getExpertRole() { return expertRole; }

    public String getAvailability() { return availability; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getLanguage1() { return language1; }

    public String getLanguage2() { return language2; }

    public String getLanguage3() { return language3; }

}
