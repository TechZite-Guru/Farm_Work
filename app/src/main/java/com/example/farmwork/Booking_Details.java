package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private String hisid, Currenthisid, booking_date, booking_day;
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
        }

        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name123 = booking_name.getText().toString().trim();
                phone123 = booking_phone.getText().toString().trim();
                village = booking_village.getText().toString().trim();
                Log.d("Success", "Going to Store");
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("User Data", "Booking Data Upload UnSuccess");
            }
        });

        Intent to_book = new Intent(Booking_Details.this, Home.class);
        startActivity(to_book);
    }
}