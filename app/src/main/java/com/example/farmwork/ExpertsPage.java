package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExpertsPage extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFirestore fStore;

    ExpertsAdapter expertsAdapter;

    List<ExpertsViewModel> experts_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts_page);

        recyclerView = findViewById(R.id.recyclerview_experts);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshExperts);

        fStore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        collectData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                experts_list.clear();
                collectData();
                expertsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void collectData() {
        CollectionReference collectionReference = fStore.collection("Experts").document("gjDN92FwKQnPT6KLFjHC").collection("English");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            ExpertsViewModel expertsViewModel = new ExpertsViewModel(
                                    documentSnapshot.getString("ExpertName"),
                                    documentSnapshot.getString("ExpertRole"),
                                    documentSnapshot.getString("Availability"),
                                    documentSnapshot.getString("Phone"),
                                    documentSnapshot.getString("Language1"),
                                    documentSnapshot.getString("Language2"),
                                    documentSnapshot.getString("Language3")
                            );
                            experts_list.add(expertsViewModel);
                        }
                    }

                    expertsAdapter = new ExpertsAdapter(experts_list, ExpertsPage.this);
                    recyclerView.setAdapter(expertsAdapter);
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
                });
    }
}