package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Double.parseDouble;

public class BookingPage extends AppCompatActivity implements LocationListener, DatesAdapter.Booking_Details {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView_7;
    String name, location, profile_image;
    Button book, to_details_btn;
    ListView listView;
    LinearLayout linearLayout;
    LocationManager mLocationManager;
    CircleImageView profile_image_holder;
    TextView distance_between, location_holder, name_holder, date;
    FirebaseFirestore fStore;
    private double lat1, long1;
    private double myLatitude, myLongitude;
    private String id, date_now, username;
    String a, b;
    DatesAdapter datesAdapter;
    ProgressDialog pd;
    String booking_date;
    List<DatesViewModel> dates_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        profile_image_holder = findViewById(R.id.image_holder);
        name_holder = findViewById(R.id.full_name);
        location_holder = findViewById(R.id.user_location);
        distance_between = findViewById(R.id.distance_between);
        pd = new ProgressDialog(this);

        pd.show();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setContentView(R.layout.progress_dialog);
        //pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        recyclerView_7 = findViewById(R.id.next_7_days);
        recyclerView_7.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_7.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        to_details_btn = findViewById(R.id.bookbtn_todetails);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh_availability);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dates_list.clear();
                pd.dismiss();
                generateDates();
                datesAdapter.notifyDataSetChanged();
            }
        });

        //linearLayout = findViewById(R.id.linear_layout_listitem);
        //linearLayout.getBackground().setAlpha(29);

        fStore = FirebaseFirestore.getInstance();

        detectCurrentLocation();


        /*name_holder.setText(getIntent().getStringExtra("name"));
        location_holder.setText(getIntent().getStringExtra("location"));
        Picasso.get().load(getIntent().getStringExtra("profile_image")).placeholder(R.drawable.ic_baseline_account_circle_24).into(profile_image_holder);*/

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            WorkerViewModel workerViewModel = (WorkerViewModel) intent.getSerializableExtra("data");
            name_holder.setText(workerViewModel.getName());
            username = workerViewModel.getName();
            location_holder.setText(workerViewModel.getLocality());
            Picasso.get().load(workerViewModel.getProfile_image()).placeholder(R.drawable.ic_baseline_account_circle_24).into(profile_image_holder);
            id = workerViewModel.getuId();
            Log.d("USERID:", id);

        }

        generateDates();

        //dates_list.add(new DatesViewModel("DATES", "ID"));
    }

    public void generateDates() {
        for (int j = 1; j <= 7; j++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, EEEE");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            Log.d("Current Date", ""+currentdate);
            currentCal.add(Calendar.DATE, j);
            String toDate = dateFormat.format(currentCal.getTime());
            getDates(toDate);
        }
    }

    public void getDates(String date) {

        fStore.collection("Worker").document(id).collection("BookedBy").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pd.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        if (task.isSuccessful()) {
                            int k = 0;
                            int count = 0;
                            int size = task.getResult().size();
                            //Log.d("TOTAL NO.OF BOOK DATES", ""+String.valueOf(size));
                            String nextdates_array[] = new String[size];
                            //String array1[] = new String[size];
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TOTAL NO.OF BOOK DATES", ""+String.valueOf(size));
                                booking_date = document.getString("booking_date");
                                if (booking_date.equals(date)) {
                                    //Log.d("Booking_Date", ""+booking_date);
                                    //Log.d("TO_DATE", "" +date);
                                    break;
                                }
                                else {
                                    //Log.d("START", "Entered into Else");
                                    //Log.d("SIZE", ""+nextdates_array.length);
                                    if (k == 0) {
                                        //Log.d("START", "Entered into Else-if");
                                        for (int t = 0; t < nextdates_array.length; t++) {
                                            nextdates_array[t] = date;
                                            k++;
                                            count++;
                                            Log.d("K", ""+k);
                                            break;
                                        }

                                    }
                                    else {
                                        //Log.d("K", ""+k);
                                        //Log.d("START", "Entered into Else-Else");
                                        nextdates_array[k] = date;
                                        count++;
                                        Log.d("Array-Length",""+count);
                                        k++;
                                    }
                                    /*for(int i = 0; i < nextdates_array.length; i++) {
                                        for(int j = i + 1; j < nextdates_array.length; j++) {
                                            if(nextdates_array[i] == nextdates_array[j]) {
                                                count++;
                                            }

                                        }
                                    }*/
                                }
                                if (count == size) {
                                    Log.d("START", "count == size");
                                    String q = nextdates_array[0];
                                    DatesViewModel datesViewModel = new DatesViewModel(q, id);
                                    dates_list.add(datesViewModel);
                                }
                                //Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            datesAdapter = new DatesAdapter(dates_list, BookingPage.this);
                            recyclerView_7.setAdapter(datesAdapter);
                        } else {
                            Log.d("TAG2", "Error getting documents: ", task.getException());
                        }
                    }
                });
        /*Query collectionReference = fStore.collection("Worker").document(id).collection("BookedBy").whereNotEqualTo("booking_date", date);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            DatesViewModel datesViewModel = new DatesViewModel(date, id);
                            dates_list.add(datesViewModel);
                        }
                    }
                }
                else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/
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
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        findDistance();
    }

    public void findDistance() {

        DocumentReference docRef = fStore.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        lat1 = documentSnapshot.getDouble("latitude");
                        long1 = documentSnapshot.getDouble("longitude");
                        Log.d("Latitude","" +lat1);
                        Log.d("Longitude","" +long1);
                        Log.d("Distance0",":"+distance_between);

                        if (lat1 != 0 && long1 != 0) {

                            double longDiff = myLongitude - long1;

                            double distance = Math.sin(deg2rad(myLatitude))
                                    *Math.sin(deg2rad(lat1))
                                    +Math.cos(deg2rad(myLatitude))
                                    *Math.cos(deg2rad(lat1))
                                    *Math.cos(deg2rad(longDiff));

                            distance = Math.acos(distance);

                            distance = rad2deg(distance);

                            distance *= 60 * 1.1515;

                            distance *= 1.609344;



                            distance_between.setText(String.format(Locale.US, "%2f KM", distance));
                            Log.d("Distance",":"+distance_between);

                        }
                    } else {
                        Log.d("TAG", "No such type of document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double myLatitude) {
        return (myLatitude*Math.PI/180.0);
    }

    @Override
    public void bookingDetails(DatesViewModel datesViewModel) {
        Intent i = new Intent(this, Booking_Details.class);
        i.putExtra("date", datesViewModel);
        i.putExtra("name", username);
        startActivity(i);
    }
}