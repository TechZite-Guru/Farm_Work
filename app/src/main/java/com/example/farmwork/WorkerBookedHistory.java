package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkerBookedHistory extends AppCompatActivity {

    ProgressDialog pd;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView no_records, network_error, upcoming;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String currentUserId, role;
    RecyclerView recyclerView1;
    List<WorkerBookedModel> worker_bookings = new ArrayList<>();
    WorkerBookedAdapter workerBookedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_booked_history);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUserId = fAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            role = intent.getStringExtra("role");
        }

        upcoming = findViewById(R.id.upcoming_title);
        network_error = findViewById(R.id.no_internet);
        no_records = findViewById(R.id.no_records);
        pd = new ProgressDialog(this);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh_booked);
        recyclerView1 = findViewById(R.id.book_history_recyclerview);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        pd.show();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setContentView(R.layout.progress_dialog);
        //pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (role.equals("Worker")) {
            collectWorkerBookedHistory();
        }
        else {
            upcoming.setText(getResources().getString(R.string.upcoming_bookings));
            collectBookerBookings();
        }

        Log.d("START", "You are in Worker Booked History Screen");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                worker_bookings.clear();
                if (role.equals("Worker")) {
                    collectWorkerBookedHistory();
                }
                else {
                    upcoming.setText(getResources().getString(R.string.upcoming_bookings));
                    collectBookerBookings();
                }
                workerBookedAdapter.notifyDataSetChanged();
            }
        });
    }

    public void collectWorkerBookedHistory() {
        fStore.collection("Worker").document(currentUserId).collection("BookedBy").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            swipeRefreshLayout.setRefreshing(false);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (task.getResult().size() == 1) {
                                    no_records.setVisibility(View.VISIBLE);
                                }
                                if ((document.getString("bookie_name") != null) &&
                                        (document.getString("bookie_phone") != null) &&
                                        (document.getString("bookie_village") != null) &&
                                        (document.getString("booking_date") != null)) {

                                    String boo_name = getResources().getString(R.string.name);
                                    String boo_phone = getResources().getString(R.string.phone_number_colon);
                                    String boo_village = getResources().getString(R.string.village);
                                    String boo_date = getResources().getString(R.string.booking_date);

                                    String name = document.getString("bookie_name");
                                    String phone = document.getString("bookie_phone");
                                    String village = document.getString("bookie_village");
                                    String date = document.getString("booking_date");
                                    WorkerBookedModel workerBookedModel = new WorkerBookedModel(boo_name, name, boo_phone, phone, boo_village, village, boo_date, date);
                                    Log.d("Name", ""+document.getString("bookie_name"));
                                    worker_bookings.add(workerBookedModel);
                                }

                            }

                            workerBookedAdapter = new WorkerBookedAdapter(worker_bookings, WorkerBookedHistory.this);
                            recyclerView1.setAdapter(workerBookedAdapter);

                        } else {
                            Log.d("TAG2", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void collectBookerBookings() {
        fStore.collection("Booker_Bookings").document(currentUserId).collection("Booked_Worker").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            swipeRefreshLayout.setRefreshing(false);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (task.getResult().size() == 1) {
                                    no_records.setVisibility(View.VISIBLE);
                                }
                                if ((document.getString("worker_name") != null) &&
                                        (document.getString("worker_phone") != null) &&
                                        (document.getString("worker_village") != null) &&
                                        (document.getString("booking_date") != null)) {

                                    String boo_name = getResources().getString(R.string.worker_name);
                                    String boo_phone = getResources().getString(R.string.phone_number_colon);
                                    String boo_village = getResources().getString(R.string.village);
                                    String boo_date = getResources().getString(R.string.booking_date);

                                    String name = document.getString("worker_name");
                                    String phone = document.getString("worker_phone");
                                    String village = document.getString("worker_village");
                                    String date = document.getString("booking_date");
                                    WorkerBookedModel workerBookedModel = new WorkerBookedModel(boo_name, name, boo_phone, phone, boo_village, village, boo_date, date);
                                    Log.d("Name", ""+document.getString("worker_name"));
                                    worker_bookings.add(workerBookedModel);
                                }

                            }

                            workerBookedAdapter = new WorkerBookedAdapter(worker_bookings, WorkerBookedHistory.this);
                            recyclerView1.setAdapter(workerBookedAdapter);

                        } else {
                            Log.d("TAG2", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}