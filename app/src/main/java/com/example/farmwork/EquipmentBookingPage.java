package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EquipmentBookingPage extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_booking_page);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        UserId = fAuth.getCurrentUser().getUid();

    }

    /*public void generateDates() {
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
                                    }*/
                                    /*for(int i = 0; i < nextdates_array.length; i++) {
                                        for(int j = i + 1; j < nextdates_array.length; j++) {
                                            if(nextdates_array[i] == nextdates_array[j]) {
                                                count++;
                                            }

                                        }
                                    }*/
                                /*}
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
                });*/
}