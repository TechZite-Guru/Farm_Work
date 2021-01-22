package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookingPage extends AppCompatActivity {

    String name, location, profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        ImageView profile_image_holder = findViewById(R.id.image_holder);
        TextView name_holder = findViewById(R.id.full_name);
        TextView location_holder = findViewById(R.id.user_location);


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
}