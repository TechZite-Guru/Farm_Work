package com.example.farmwork;

import static java.lang.Double.parseDouble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EquipmentShops extends AppCompatActivity implements ShopsListAdapter.ShopPage {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    ShopsListAdapter shopsListAdapter;

    FirebaseFirestore fStore;

    String category, p, shop_state;

    List<ShopsViewModel> shops_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_shops);

        recyclerView = findViewById(R.id.recyclerview_shops);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        fStore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            category = intent.getStringExtra("category");
        }

        SharedPreferences preferences = this.getSharedPreferences("Address", Context.MODE_PRIVATE);
        if (preferences.getString("User_Pin", "") != null) {
            p = preferences.getString("User_Pin", "");
            shop_state = preferences.getString("Admin_Area", "");

            collectData();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shops_list.clear();
                collectData();
                shopsListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void collectData() {
        Query collectionReference = fStore.collection("Shops").document("7RZwoC8EztkcyfZaYSbr").collection(category).whereEqualTo("ShopState", shop_state);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            ShopsViewModel shopsViewModel = new ShopsViewModel(
                                    documentSnapshot.getString("ShopName"),
                                    documentSnapshot.getString("ShopDoorNo"),
                                    documentSnapshot.getString("ShopStreet"),
                                    documentSnapshot.getString("ShopVillage"),
                                    documentSnapshot.getString("ShopTown"),
                                    documentSnapshot.getString("ShopState"),
                                    documentSnapshot.getString("ShopPincode"),
                                    documentSnapshot.getString("ShopAddress"),
                                    documentSnapshot.getString("Available"),
                                    documentSnapshot.getString("Description"),
                                    documentSnapshot.getString("TotalCutters"),
                                    documentSnapshot.getString("TotalHarvesters"),
                                    documentSnapshot.getString("TotalPlanters"),
                                    documentSnapshot.getString("TotalSprayers"),
                                    documentSnapshot.getString("TotalShredders"),
                                    documentSnapshot.getString("TotalTillers"),
                                    documentSnapshot.getString("TotalSeeders"),
                                    documentSnapshot.getString("TotalTractors"),
                                    documentSnapshot.getString("TotalWeeders")
                                    );
                            shops_list.add(shopsViewModel);
                        }
                    }

                    shopsListAdapter = new ShopsListAdapter(shops_list, EquipmentShops.this);
                    recyclerView.setAdapter(shopsListAdapter);
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

    @Override
    public void shopPage(ShopsViewModel shopsViewModel) {
        startActivity(new Intent(this, ShopPage.class).putExtra("data", shopsViewModel));
    }
}