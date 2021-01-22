package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class BookingPage extends AppCompatActivity implements LocationListener {

    String name, location, profile_image;
    LocationManager mLocationManager;
    ImageView profile_image_holder;
    TextView distance_between, location_holder, name_holder;
    FirebaseFirestore fStore;
    double lat1, long1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        profile_image_holder = findViewById(R.id.image_holder);
        name_holder = findViewById(R.id.full_name);
        location_holder = findViewById(R.id.user_location);
        distance_between = findViewById(R.id.distance_between);

        fStore = FirebaseFirestore.getInstance();

        detectCurrentLocation();


        /*name_holder.setText(getIntent().getStringExtra("name"));
        location_holder.setText(getIntent().getStringExtra("location"));
        Picasso.get().load(getIntent().getStringExtra("profile_image")).placeholder(R.drawable.ic_baseline_account_circle_24).into(profile_image_holder);*/

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            WorkerViewModel workerViewModel = (WorkerViewModel) intent.getSerializableExtra("data");
            name_holder.setText(workerViewModel.getName());
            location_holder.setText(workerViewModel.getLocation());
            Picasso.get().load(workerViewModel.getProfile_image()).placeholder(R.drawable.ic_baseline_account_circle_24).into(profile_image_holder);

        }
    }
    private void detectCurrentLocation() {
        Toast.makeText(this, "Please wait, getting your Location", Toast.LENGTH_SHORT).show();
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double myLatitude = location.getLatitude();
        double myLongitude = location.getLongitude();
        findDistance(myLatitude, myLongitude);
    }

    public void findDistance(double myLatitude, double myLongitude) {

        CollectionReference documentReference = fStore.collection("users");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        lat1 = documentSnapshot.getDouble("latitude");
                        long1 = documentSnapshot.getDouble("longitude");
                    }
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });

        Toast.makeText(this, "Latitude", Toast.LENGTH_SHORT).show();

        double longDiff = myLongitude - long1;

        double distance = Math.sin(deg2rad(myLatitude))
                *Math.sin(deg2rad(lat1))
                +Math.cos(deg2rad(myLatitude))
                *Math.cos(deg2rad(lat1))
                *Math.cos(deg2rad(longDiff));

        distance = Math.acos(distance);

        distance = rad2deg(distance);

        distance = distance * 1.609344;

        distance_between.setText(String.format(Locale.US, "%2f KM", distance));

    }

    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double myLatitude) {
        return (myLatitude*Math.PI/180.0);
    }

}