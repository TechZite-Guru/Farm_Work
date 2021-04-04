package com.example.farmwork;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog pd;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        setHasOptionsMenu(true);

        Log.d("SUCCESS", "In Notification Fragment");

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUserId = fAuth.getCurrentUser().getUid();

        pd = new ProgressDialog(getActivity());
        pd.show();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setContentView(R.layout.progress_dialog);

        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh_notifications);

        no_notifications = root.findViewById(R.id.no_notifies);
        recyclerView = root.findViewById(R.id.recyclerview_notifications);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        loadUserRole();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notification_list.clear();
                loadUserRole();
                notificationAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    public void loadUserRole() {
        fStore.collection("users").document(currentUserId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    role = documentSnapshot.getString("roles");
                    generateDates();
                }
            }
        });
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
            if (role.equals("Worker")) {
                loadBookerPastNotification(booking_day);
                loadWorkerPastNotification(booking_day);
            }
            else {
                loadBookerPastNotification(booking_day);
            }
        }
    }


    /*public void loadBookerNotification() {
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
    }*/

    public void loadBookerPastNotification(String day) {
        fStore.collection("Booker_Past_notifications").document(currentUserId+day).collection("workerID").orderBy("timestamp").limitToLast(20).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 1) {
                                pd.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                                no_notifications.setVisibility(View.VISIBLE);
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getString("notification").equals("Your Booker_Past_notifications goes here")) {
                                    notification_list.add(new NotificationViewModel(document.getString("notification")));
                                    if (!role.equals("Worker")) {
                                        pd.dismiss();
                                        swipeRefreshLayout.setRefreshing(false);
                                        notificationAdapter = new NotificationAdapter(notification_list, NotificationFragment.this);
                                        recyclerView.setAdapter(notificationAdapter);
                                    }
                                }
                            }
                        }
                    }
                });
    }

   /* public void loadWorkerNotification() {
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
    }*/

    public void loadWorkerPastNotification(String day) {
        fStore.collection("Past_notifications").document(currentUserId+day).collection("workerID").orderBy("timestamp").limitToLast(20).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 1) {
                                no_notifications.setVisibility(View.VISIBLE);
                                no_notifications.setVisibility(View.GONE);
                            }
                            else {
                                no_notifications.setVisibility(View.GONE);
                            }
                            pd.dismiss();
                            swipeRefreshLayout.setRefreshing(false);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getString("notification").equals("Your Worker Past Notifications text goes here")) {
                                    notification_list.add(new NotificationViewModel(document.getString("notification")));
                                    notificationAdapter = new NotificationAdapter(notification_list, NotificationFragment.this);
                                    recyclerView.setAdapter(notificationAdapter);
                                }
                            }
                        }
                    }
        });
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