package com.example.farmwork;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationFragment extends Fragment {

    TextView no_notifications;
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String currentUserId;
    NotificationAdapter notificationAdapter;
    private String role;
    List<NotificationViewModel> notification_list = new ArrayList<>();
    String booker_notification;
    String worker_notification;
    int count = 0, count2 = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        setHasOptionsMenu(true);

        Log.d("SUCCESS", "In Notification Fragment");

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUserId = fAuth.getCurrentUser().getUid();

        no_notifications = root.findViewById(R.id.no_notifies);
        recyclerView = root.findViewById(R.id.recyclerview_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        fStore.collection("users").document(currentUserId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    role = documentSnapshot.getString("roles");
                    loadBookerNotification();
                }
            }
        });

        /*fStore.collection("users").document(currentUserId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    String role = documentSnapshot.getString("roles");
                    if (role.equals("Worker")) {

                    }
                }
            }
        });*/
        return root;
    }

    public void generateDates() {
        for (int j = 1; j <= 7; j++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, EEEE");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            Log.d("Current Date", ""+currentdate);
            currentCal.add(Calendar.DATE, j);
            String toDate = dateFormat.format(currentCal.getTime());
            String booking_day = toDate.replaceAll("[^a-zA-Z]", "");
            loadBookerPastNotification(booking_day);
        }
    }


    public void loadBookerNotification() {
        fStore.collection("Booker_Notifications").document(currentUserId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if (!documentSnapshot.getString("notification").equals("Your Booker_Notifications text goes here")) {
                        booker_notification = documentSnapshot.getString("notification");
                    }
                    if (role.equals("Worker")) {
                        loadWorkerNotification();
                    }
                    else {
                        generateDates();
                    }
                }
            }
        });
    }

    public void loadBookerPastNotification(String day) {
        fStore.collection("Booker_Past_notifications").document(currentUserId+day).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if (!documentSnapshot.getString("notification").equals("Your Booker_Past_notifications goes here")) {
                        count++;
                        if (!booker_notification.equals(documentSnapshot.getString("notification"))) {
                            notification_list.add(new NotificationViewModel(booker_notification));
                            notification_list.add(new NotificationViewModel(documentSnapshot.getString("notification")));
                        }
                        else {
                            notification_list.add(new NotificationViewModel(booker_notification));
                        }
                        if (role.equals("Worker")) {
                            loadWorkerPastNotification(day);
                        }
                        else {
                            notificationAdapter = new NotificationAdapter(notification_list, NotificationFragment.this);
                            recyclerView.setAdapter(notificationAdapter);
                        }
                    }

                    if (role.equals("Worker")) {
                        loadWorkerPastNotification(day);
                    }
                }
            }
        });
        if (count == 0) {
            no_notifications.setVisibility(View.VISIBLE);
        }
    }

    public void loadWorkerNotification() {
        fStore.collection("Notifications").document(currentUserId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if (!documentSnapshot.getString("notification").equals("Your Worker Notification text goes here")) {
                        worker_notification = documentSnapshot.getString("notification");
                    }
                    generateDates();
                }
            }
        });
    }

    public void loadWorkerPastNotification(String day) {
        fStore.collection("Past_notifications").document(currentUserId+day).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if (!documentSnapshot.getString("notification").equals("Your Worker Past Notifications text goes here")) {
                        count2++;
                        if (!worker_notification.equals(documentSnapshot.getString("notification"))) {
                            notification_list.add(new NotificationViewModel(worker_notification));
                            notification_list.add(new NotificationViewModel(documentSnapshot.getString("notification")));
                        }
                        else {
                            notification_list.add(new NotificationViewModel(worker_notification));
                        }
                        notificationAdapter = new NotificationAdapter(notification_list, NotificationFragment.this);
                        recyclerView.setAdapter(notificationAdapter);
                    }
                }
            }
        });
        if (count2 == 0) {
            no_notifications.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_service);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

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