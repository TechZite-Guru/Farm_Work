package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {

    LinearLayout layout_eng, layout_hindi, layout_tel;
    Context context;
    TextView text_hindi_lang, text_tel_lang, text_eng_lang;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchWelcomeActivity();
            finish();
        }

        setContentView(R.layout.activity_select_language);

        layout_eng = findViewById(R.id.english_lang);
        layout_hindi = findViewById(R.id.hindi_lang);
        layout_tel = findViewById(R.id.tel_lang);

        text_eng_lang = findViewById(R.id.eng_lang_text);
        text_hindi_lang = findViewById(R.id.hindi_lang_text);
        text_tel_lang = findViewById(R.id.tel_lang_text);



        layout_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_eng_lang.getText().toString();
                Intent to_welcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                to_welcome.putExtra("mytext",language);
                setLocale("en");
                recreate();
                startActivity(to_welcome);
            }
        });

        layout_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_hindi_lang.getText().toString();
                Intent to_welcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                to_welcome.putExtra("mytext",language);
                setLocale("hi");
                recreate();
                startActivity(to_welcome);
            }
        });

        layout_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_tel_lang.getText().toString();
                Intent to_welcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                to_welcome.putExtra("mytext",language);
                setLocale("te");
                recreate();
                startActivity(to_welcome);
            }
        });
    }

    private void launchWelcomeActivity() {

            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(SelectLanguage.this, Home.class));

    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //Saving data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Selected_lang",lang);
        editor.apply();
    }
    //load data from shared preferences -language_selected
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("Selected_lang", "");
        setLocale(language);
    }
}