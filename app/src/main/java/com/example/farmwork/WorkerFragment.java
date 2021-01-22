package com.example.farmwork;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

public class WorkerFragment extends Fragment implements CategoryAdapter.BookingPage {

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ImageView prefix;
    String currentUserID;
    SwipeRefreshLayout swipeRefreshLayout;
    BookingPage bookingPage;

    List<WorkerViewModel> worker_list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_worker, container, false);

        setHasOptionsMenu(true);

        recyclerView = root.findViewById(R.id.recyclerview);
        prefix = root.findViewById(R.id.prefix);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUserID = fAuth.getCurrentUser().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        collectData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                worker_list.clear();
                collectData();
                categoryAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
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
        CollectionReference documentReference = fStore.collection("users");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        WorkerViewModel workerViewModel = new WorkerViewModel(documentSnapshot.getString("email"),
                                documentSnapshot.getString("name"),
                                documentSnapshot.getString("phone"),
                                documentSnapshot.getString("location"),
                                documentSnapshot.getString("profile_image"));
                        worker_list.add(workerViewModel);
                    }

                    categoryAdapter = new CategoryAdapter(worker_list, WorkerFragment.this);
                    recyclerView.setAdapter(categoryAdapter);
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
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getContext(), "Going to searchData()", Toast.LENGTH_SHORT).show();
                categoryAdapter.getFilter().filter(s);
                Toast.makeText(getContext(), "Came Out", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    /*public void searchData(String s) {
        fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("users");
        Query query = collectionReference.whereEqualTo("search_name", s.toLowerCase());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Toast.makeText(getContext(), "In searchData()", Toast.LENGTH_SHORT).show();

                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView2.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL) {
                    @Override
                    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                        // Do not draw the divider
                    }
                });
                FirestoreRecyclerOptions<WorkerViewModel> options =
                        new FirestoreRecyclerOptions.Builder<WorkerViewModel>()
                                .setQuery(FirebaseFirestore.getInstance().collection("users"), WorkerViewModel.class)
                                .build();

                Toast.makeText(getContext(), "Success Search :" +s, Toast.LENGTH_SHORT).show();

                //categoryAdapter = new CategoryAdapter();
                recyclerView2.setAdapter(categoryAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed Search", Toast.LENGTH_SHORT).show();
            }
        });

        /*query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        Toast.makeText(getContext(), "In searchData()", Toast.LENGTH_SHORT).show();

                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.VISIBLE);
                        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView2.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL) {
                            @Override
                            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                                // Do not draw the divider
                            }
                        });
                        FirestoreRecyclerOptions<WorkerViewModel> options =
                                new FirestoreRecyclerOptions.Builder<WorkerViewModel>()
                                        .setQuery(FirebaseFirestore.getInstance().collection("users"), WorkerViewModel.class)
                                        .build();

                        Toast.makeText(getContext(), "Success Search :" +s, Toast.LENGTH_SHORT).show();

                        categoryAdapter = new CategoryAdapter(options);
                        recyclerView2.setAdapter(categoryAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed Search", Toast.LENGTH_SHORT).show();
                    }
                });*/

    /*@Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i=0; i<names.length; i++){
            String a = names[i];
            for (int j=i; j<logos.length; j++){
                int b = logos[j];
                category_list.add(new WorkerViewModel(b, a));
                break;
            }
        }


    }*/
}