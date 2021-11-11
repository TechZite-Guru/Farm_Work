package com.example.farmwork;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Home extends AppCompatActivity implements LocationListener {

    RecyclerView search_recyclerview, recyclerView;
    LocationManager mLocationManager;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String currentUserID;
    TextView progress_text;
    WorkerFragment workerFragment;
    CategoryAdapter categoryAdapter;
    private String full_address, locality, postalcode, adminarea, town;
    private double latitude, longitude;
    private String lat, lon;
    ProgressDialog pd;
    Button turn_gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_workers, R.id.navigation_machinery, R.id.navigation_notification, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        gettingCheck();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUserID = fAuth.getCurrentUser().getUid();

        progress_text = findViewById(R.id.progress_text);
        recyclerView = findViewById(R.id.recyclerview);

        locationAlertDialog();
    }

    public void gettingCheck() {
        boolean check=checkConnection();
        if (check == true) {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            /*pd.setMessage(getResources().getString(R.string.getting_location_please_check_gps_location));
            pd.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.location_setup), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });*/
            pd.show();
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setContentView(R.layout.progress_location);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                detectCurrentLocation();
            }
        }

        else {
            networkErrorAlertDialog();
        }
    }

    private void detectCurrentLocation() {
        Toast.makeText(this, "Please wait, getting your Location", Toast.LENGTH_SHORT).show();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        findAddress();
    }

    private void findAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);

            full_address = addresses.get(0).getAddressLine(0);
            locality = addresses.get(0).getLocality();
            adminarea = addresses.get(0).getAdminArea();
            town = addresses.get(0).getSubAdminArea();
            postalcode = addresses.get(0).getPostalCode();

            if (full_address != null){
                Map<String, Object> location = new HashMap<>();
                location.put("location", full_address);
                location.put("latitude", latitude);
                location.put("longitude", longitude);
                location.put("locality", locality);
                location.put("adminarea", adminarea);
                location.put("town", town);
                location.put("postalcode", postalcode);
                fStore.collection("users").document(currentUserID).update(location).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Location", "Location Updated Succesfully");
                    }
                });
                pd.dismiss();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("LATITUDE_HOME", "" +latitude);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.000000000000000");
        lat = decimalFormat.format(latitude);
        lon = decimalFormat.format(longitude);
        setLocale(postalcode, lat, lon, adminarea);
    }

    public void setLocale(String pin, String my_Latitude, String my_Longitude, String adminarea) {
        SharedPreferences.Editor pincode = getSharedPreferences("Address", MODE_PRIVATE).edit();
        pincode.putString("User_Pin",pin);
        pincode.putString("User_Latitude", my_Latitude);
        pincode.putString("User_Longitude", my_Longitude);
        pincode.putString("Admin_Area", adminarea);
        Log.d("LATITUDE_HOME", "" +my_Latitude);
        pincode.apply();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Please turn on your Location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                detectCurrentLocation();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    int doubleBackToExitPressed = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        } else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed = 1;
            }
        }, 2000);
    }

    private void locationAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String phnumber = "Please Turn ON GPS if it is OFF. It is mandatory.";
        alertDialog.setCancelable(false);
        alertDialog.setTitle("GPS Location");
        alertDialog.setMessage(phnumber);
        alertDialog.setPositiveButton("   Turn ON GPS   ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("   Cancel  ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(getResources().getColor(R.color.orange_500));
        pbutton.setTextColor(Color.WHITE);
    }

    protected boolean checkConnection(){
        ConnectivityManager conMan = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

        final boolean connected = networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected();

        if ( !connected) {
            Toast.makeText(this, "Failed to connect to internet.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void networkErrorAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String message = "Please Check your Internet Connection";
        alertDialog.setCancelable(false);
        alertDialog.setTitle("No Internet");
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("   Retry   ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gettingCheck();
            }
        });
        alertDialog.setNegativeButton("   Close  ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                System.exit(0);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(getResources().getColor(R.color.orange_500));
        pbutton.setTextColor(Color.WHITE);
    }
}