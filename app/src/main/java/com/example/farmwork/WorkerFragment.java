package com.example.farmwork;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Double.parseDouble;

public class WorkerFragment extends Fragment implements CategoryAdapter.BookingPage {

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CircleImageView prefix;
    String currentUserID;
    SwipeRefreshLayout swipeRefreshLayout;
    BookingPage bookingPage;
    String Name, p;
    ProgressBar progressBar1;
    ProgressDialog pd;
    Home home;
    int position = 0;
    private double myLatitude, myLongitude;
    private String lat1, long1;

    List<String> suggestion_list = new ArrayList<>();
    List<WorkerViewModel> worker_list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_worker, container, false);

        setHasOptionsMenu(true);
        recyclerView = root.findViewById(R.id.recyclerview);
        prefix = root.findViewById(R.id.prefix);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        pd = new ProgressDialog(getContext());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUserID = fAuth.getCurrentUser().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        pd.show();
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setContentView(R.layout.progress_dialog);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Address", Context.MODE_PRIVATE);
        p = preferences.getString("User_Pin", "");
        lat1 = preferences.getString("User_Latitude", "");
        long1 = preferences.getString("User_Longitude", "");

        myLatitude = parseDouble(lat1);
        myLongitude = parseDouble(long1);

        Log.d("PINCODE", "" + p);
        Log.d("LATITUDE", "" + myLatitude);
        collectData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                worker_list.clear();
                pd.dismiss();
                collectData();
                categoryAdapter.notifyDataSetChanged();
            }
        });


        //worker_fragment_back.getBackground().setAlpha(200);
        /*categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                WorkerViewModel model = documentSnapshot.toObject(WorkerViewModel.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Log.d("Path:", "Path" +path);
                Toast.makeText(getContext(), "Id:" + id, Toast.LENGTH_SHORT).show();
                Intent to_booking_page = new Intent(getContext(), BookingPage.class);
                to_booking_page.putExtra("mytext",path);
                startActivity(to_booking_page);
            }
        });*/
        return root;

    }

    private void collectData() {
        Query collectionReference = fStore.collection("users").whereEqualTo("postalcode", p);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                pd.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            WorkerViewModel workerViewModel = new WorkerViewModel(documentSnapshot.getString("email"),
                                    documentSnapshot.getString("name"),
                                    documentSnapshot.getString("phone"),
                                    documentSnapshot.getString("adminarea"),
                                    documentSnapshot.getString("locality"),
                                    documentSnapshot.getString("profile_image"),
                                    documentSnapshot.getDouble("latitude"),
                                    documentSnapshot.getDouble("longitude"),
                                    documentSnapshot.getId(), myLatitude, myLongitude);
                            worker_list.add(workerViewModel);
                        }
                    }

                    categoryAdapter = new CategoryAdapter(worker_list, WorkerFragment.this);
                    recyclerView.setAdapter(categoryAdapter);
                }
                else {
                    pd.dismiss();
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
   /*@Override
    public void onStart() {
       super.onStart();
       collectData();
   }

    /*@Override
    public void onStop(){
        super.onStop();
        categoryAdapter.stopListening();
    }*/

    @Override
    public void bookingPage(WorkerViewModel workerViewModel) {
        startActivity(new Intent(getContext(), BookingPage.class).putExtra("data", workerViewModel));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_service);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search by Name, Place.....");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getContext(), "Going to searchData()", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(s)) {
                    categoryAdapter.getFilter().filter(s);
                    searchView.setQuery("", false);
                    searchView.setIconifiedByDefault(true);
                }
                Toast.makeText(getContext(), "Came Out", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    categoryAdapter.getFilter().filter(newText);
                    //searchView.setQuery("", false);
                    //searchView.setIconified(true);
                }
                return false;
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.search_service){
            return true;
        }

        if (id == R.id.lang_support){
            PrefManager prefManager = new PrefManager(getContext());

            // make first time launch TRUE
            prefManager.setFirstTimeLaunch(true);

            startActivity(new Intent(getContext(), SelectLanguage.class));
        }

        if (id == R.id.help) {
            contactAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    private void contactAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        String phnumber = "+919398274873";
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Call Us");
        alertDialog.setMessage(phnumber);
        alertDialog.setPositiveButton("   CALL   ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callintent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phnumber, null));
                getContext().startActivity(callintent);
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
}