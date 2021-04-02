package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Booking_Details extends AppCompatActivity {

    TextView selected_date;
    Button confirm_booking;
    EditText booking_name, booking_phone, booking_village;
    String name123, phone123, village;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference documentReference;
    private String hisid, Currenthisid, booking_date, booking_day, worker_name;
    String[] days = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__details);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        Currenthisid = fAuth.getCurrentUser().getUid();

        selected_date = findViewById(R.id.selected_date);
        booking_name = findViewById(R.id.bookie_name);
        booking_phone = findViewById(R.id.bookie_phnumber);
        booking_village = findViewById(R.id.bookie_village);
        confirm_booking = findViewById(R.id.confirm_book_btn);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            DatesViewModel datesViewModel = (DatesViewModel) intent.getSerializableExtra("date");
            selected_date.setText(String.format("Booking Date: %s", datesViewModel.getDate()));
            booking_date = datesViewModel.getDate();
            hisid = datesViewModel.getId2();
            Log.d("Day", ""+booking_day);
            worker_name = intent.getStringExtra("name");
        }

        booking_day = booking_date.replaceAll("[^a-zA-Z]", "");
        Log.d("Booking_Date", ""+booking_day);

        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name123 = booking_name.getText().toString().trim();
                phone123 = booking_phone.getText().toString().trim();
                village = booking_village.getText().toString().trim();
                Log.d("Success", "Going to Store");

                if (TextUtils.isEmpty(name123)) {
                    booking_name.setError("Name is required");
                    booking_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone123)){
                    booking_phone.setError("Phone Number is required");
                    booking_phone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(village)){
                    booking_village.setError("Village name is required");
                    booking_village.requestFocus();
                    return;
                }
                storingBookingData();
            }
        });
    }

    public void storingBookingData() {
        Log.d("Success", "In Store");
        Map<String, Object> book = new HashMap<>();
        book.put("bookie_name", name123);
        book.put("bookie_phone", phone123);
        book.put("bookie_village", village);
        book.put("booking_date", booking_date);

        Map<String, Object> booked = new HashMap<>();
        booked.put("booking_date", booking_date);
        booked.put("worker_id", hisid);

        Log.d("Success", "Crossed Map");
        Log.d("ID: ", "" +hisid);

        fStore.collection("Worker").document(hisid).collection("BookedBy").document(Currenthisid+booking_day).set(book).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Booking Data", "Booking Data Upload Success");
                bookingAlertDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("User Data", "Booking Data Upload UnSuccess");
            }
        });
    }
    private void bookingAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String name = worker_name.toUpperCase();
        String message = "You have Successfully booked the Worker "+name+" for the date "+booking_date;
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Booking Success");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("   OK   ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent to_home = new Intent(Booking_Details.this, Home.class);
                startActivity(to_home);
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(getResources().getColor(R.color.orange_500));
        pbutton.setTextColor(Color.WHITE);
    }
}