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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {

    LinearLayout layout_eng, layout_hindi, layout_tel, layout_kan, layout_mal, layout_tamil;
    Context context;
    TextView text_hindi_lang, text_tel_lang, text_eng_lang;
    private PrefManager prefManager;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchMainActivity();
            finish();
        }

        setContentView(R.layout.activity_select_language);

        layout_eng = findViewById(R.id.english_lang);
        layout_hindi = findViewById(R.id.hindi_lang);
        layout_tel = findViewById(R.id.tel_lang);
        layout_kan = findViewById(R.id.kan_lang);
        layout_mal = findViewById(R.id.mal_lang);
        layout_tamil = findViewById(R.id.tamil_lang);

        text_eng_lang = findViewById(R.id.eng_lang_text);
        text_hindi_lang = findViewById(R.id.hindi_lang_text);
        text_tel_lang = findViewById(R.id.tel_lang_text);

        layout_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_eng_lang.getText().toString();
                Intent to_welcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                setLocale("en");
                startActivity(to_welcome);
            }
        });

        layout_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_hindi_lang.getText().toString();
                Intent to_welcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                //to_welcome.putExtra("mytext",language);
                setLocale("hi");
                startActivity(to_welcome);
            }
        });

        layout_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_tel_lang.getText().toString();
                Intent to_welcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                //to_welcome.putExtra("mytext",language);
                setLocale("te");
                startActivity(to_welcome);
            }
        });
        layout_kan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectLanguage.this, "This language support is under maintanance. Sorry for the inconvenience. Please select another language", Toast.LENGTH_LONG).show();
            }
        });
        layout_mal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectLanguage.this, "This language support is under maintanance. Sorry for the inconvenience. Please select another language", Toast.LENGTH_LONG).show();
            }
        });
        layout_tamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectLanguage.this, "This language support is under maintanance. Sorry for the inconvenience. Please select another language", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void launchMainActivity() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SelectLanguage.this, LoginActivity.class));
    }

    private void setLocale(String lang) {
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