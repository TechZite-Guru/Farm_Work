package com.example.farmwork;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public RecyclerView recyclerView;
    Toolbar toolbar;
    Button to_bookedhistory_bttn, expertsBtn, artclesBtn;
    CategoryAdapter categoryAdapter;
    CarouselView carouselView;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private String currentUserId;
    private String role;
    ListenerRegistration registration;

    int[] sampleImages = {R.drawable.farmland, R.drawable.worker, R.drawable.machinery};

    List<HomeViewModel> category_list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUserId = fAuth.getCurrentUser().getUid();

        to_bookedhistory_bttn = root.findViewById(R.id.bookedHistory_btn);

        carouselView = root.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        expertsBtn = root.findViewById(R.id.expertsBtn);
        artclesBtn = root.findViewById(R.id.articlesBtn);

        takeRole();

        expertsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toExperts = new Intent(getContext(), ExpertsPage.class);
                startActivity(toExperts);
            }
        });

        artclesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toArticles = new Intent(getContext(), ArticlesCategories.class);
                startActivity(toArticles);
            }
        });

        return root;
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void takeRole() {
        registration = fStore.collection("users").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    role = documentSnapshot.getString("roles");
                    if (role.equals("Worker")) {
                        to_bookedhistory_bttn.setText(getResources().getString(R.string.worker_tasks_btn));
                        to_bookedhistory_bttn.setVisibility(View.VISIBLE);
                        to_bookedhistory_bttn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent to_book_history = new Intent(getContext(), WorkerBookedHistory.class);
                                to_book_history.putExtra("role", role);
                                startActivity(to_book_history);
                            }
                        });
                    }
                    else {
                        to_bookedhistory_bttn.setText(getResources().getString(R.string.booker_books_btn));
                        to_bookedhistory_bttn.setVisibility(View.VISIBLE);
                        to_bookedhistory_bttn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent to_book_history = new Intent(getContext(), WorkerBookedHistory.class);
                                to_book_history.putExtra("role", role);
                                startActivity(to_book_history);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        registration.remove();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_service);

        //SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setMaxWidth(Integer.MAX_VALUE);

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.search_service) {
            return true;
        }

        if (id == R.id.lang_support) {
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