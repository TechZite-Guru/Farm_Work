package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class WorkerBookedHistory extends AppCompatActivity {

    TextView worker_name, worker_village, cost;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_booked_history);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        worker_name = findViewById(R.id.worker_name);
        worker_village = findViewById(R.id.worker_location);
        cost = findViewById(R.id.worker_fare);

        Query documentReference = fStore.collection("Worker").document().collection("BookedBy").whereEqualTo("bookie_village", "Ballari");
    }
}