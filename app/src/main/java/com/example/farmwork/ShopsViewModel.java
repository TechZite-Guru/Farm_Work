package com.example.farmwork;

import java.io.Serializable;

public class ShopsViewModel implements Serializable {

    String shopName, shopState, shopVillage, shopStreet, shopDoorNo, description, shoppincode, shopTown, shopAddress, phoneNumber;
    String available_count, totalcut, totalharvest, totalplanter, totalspray, totalshredders, totaltillers, totalseeding, totaltractors, totalweeders;

    ShopsViewModel(String shopname, String shopDoorNo, String shopStreet, String shopVillage, String shopTown, String shopState, String shoppincode, String shopAddress, String phoneNumber, String available_count, String description, String totalcut, String totalharvest, String totalplanter, String totalspray, String totalshredders, String totaltillers, String totalseeding, String totaltractors, String totalweeders) {
        this.shopState = shopState;
        this.shopDoorNo = shopDoorNo;
        this.shopStreet = shopStreet;
        this.shopName = shopname;
        this.shopTown = shopTown;
        this.shopVillage = shopVillage;
        this.shoppincode = shoppincode;
        this.shopAddress = shopAddress;
        this.phoneNumber = phoneNumber;
        this.available_count = available_count;
        this.description = description;
        this.totalcut = totalcut;
        this.totalharvest = totalharvest;
        this.totalplanter = totalplanter;
        this.totalseeding = totalseeding;
        this.totaltillers = totaltillers;
        this.totalspray = totalspray;
        this.totalshredders = totalshredders;
        this.totaltractors = totaltractors;
        this.totalweeders = totalweeders;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopDoorNo() { return shopDoorNo; }

    public String getShopStreet() { return shopStreet; }

    public String getShopVillage() { return shopVillage; }

    public String getShopTown() { return shopTown; }

    public String getShopState() {
        return shopState;
    }

    public String getShoppincode() { return shoppincode; }

    public String getShopAddress() { return shopAddress; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getAvailable_count() { return available_count; }

    public String getDescription() { return description; }

    public String getTotalcut() { return totalcut; }

    public String getTotalharvest() { return totalharvest; }

    public String getTotalplanter() { return totalplanter; }

    public String getTotalspray() { return totalspray; }

    public String getTotalshredders() { return totalshredders; }

    public String getTotaltillers() { return totaltillers; }

    public String getTotalseeding() { return totalseeding; }

    public String getTotaltractors() { return totaltractors; }

    public String getTotalweeders() { return totalweeders; }
}
