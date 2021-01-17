package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;

    List<CategoryModel> category_list = new ArrayList<>();

    int[] logos = {R.drawable.ic_baseline_account_circle_24, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount};

    String[] names = {"Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Arrays.sort(names);
        Arrays.sort(logos);

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        for (int i=0; i<names.length; i++){
            String a = names[i];
            for (int j=i; j<logos.length; j++){
                int b = logos[j];
                category_list.add(new CategoryModel(b, a));
                break;
            }
        }



        /*category_list.add(new CategoryModel(R.drawable.ic_twotone_account_balance_24, "BANKING SECTOR"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_train_24, "TRAVEL / RESERVATION"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_food_bank_24, "FOOD DELIVERY"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_filter_tilt_shift_24, "STATE GOVERNMENT"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_group_work_24, "CENTRAL GOVERNMENT"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_shopping_cart_24, "ONLINE SHOPPING"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_speaker_phone_24, "TELECOM"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_live_tv_24, "DTH"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_monetization_on_24, "INSURANCE"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_power_24, "ELECTRICITY"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_local_gas_station_24, "GAS BOOKING"));
        category_list.add(new CategoryModel(R.drawable.ic_twotone_trending_up_24, "ONLINE TRADING"));*/

    }

    /*Bundle bundle = getIntent().getExtras();
        text = bundle.getString("mytext");
        if (text == "हिंदी") {
            setLocale("hi");
            recreate();
        }
        if (text == "తెలుగు") {
            setLocale("te");
            recreate();
        }*/
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}