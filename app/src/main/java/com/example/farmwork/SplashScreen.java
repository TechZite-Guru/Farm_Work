package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class SplashScreen extends Activity {

    Handler handler;
    ProgressBar progressBar;
    ProgressDialog pd;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);

        progressBar = findViewById(R.id.progress_circular);

        checkAuth();
    }

    private void checkAuth() {

        if (fAuth.getCurrentUser() != null) {
            Log.d("ALREADY_LOGIN", "LOGIN Success with Phone Verification");
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        pd.dismiss();
                        if (document.exists()) {
                            Log.d("TAG", "Document found" +userID);
                            Intent to_home = new Intent(SplashScreen.this,Home.class);
                            startActivity(to_home);
                        } else {
                            Log.d("TAG", "Document Not found. Staying in this Activity" +userID);
                            startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
                        }
                    } else {
                        Log.d("TAG", "Failed with: ", task.getException());
                    }
                }
            });
            loadLocale();
        }
        else {
            delayHandler();
        }
    }

    private void delayHandler() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,SelectLanguage.class);
                Log.d("HANDLER","Going To the Handler");
                startActivity(intent);
                finish();
            }
        },3000);
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        if (prefs.getString("Selected_lang", "") != null) {
            String language = prefs.getString("Selected_lang", "");
            setLocale(language);
        }
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}