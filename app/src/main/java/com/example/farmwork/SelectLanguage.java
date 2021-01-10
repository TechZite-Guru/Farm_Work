package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {

    LinearLayout layout_eng, layout_hindi, layout_tel;
    Context context;
    TextView text_hindi_lang, text_tel_lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        layout_eng = findViewById(R.id.english_lang);
        layout_hindi = findViewById(R.id.hindi_lang);
        layout_tel = findViewById(R.id.tel_lang);

        text_hindi_lang = findViewById(R.id.hindi_lang_text);
        text_tel_lang = findViewById(R.id.tel_lang_text);

        layout_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_hindi_lang.getText().toString();
                Intent to_main = new Intent(getApplicationContext(), MainActivity.class);
                to_main.putExtra("mytext",language);
                setLocale("hi");
                recreate();
                startActivity(to_main);
            }
        });

        layout_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = text_tel_lang.getText().toString();
                Intent to_main = new Intent(getApplicationContext(), MainActivity.class);
                to_main.putExtra("mytext",language);
                setLocale("te");
                recreate();
                startActivity(to_main);
            }
        });
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        Intent to_home = new Intent(getApplicationContext(), MainActivity.class);
    }
}