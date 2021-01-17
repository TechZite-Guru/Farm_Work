package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class SplashScreen extends Activity {

    Handler handler;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progress_circular);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 161, 0), PorterDuff.Mode.SRC_ATOP);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,SelectLanguage.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }
}