package com.example.farmwork;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WorkerFragment extends Fragment {

    private WorkerViewModel dashboardViewModel;
    LinearLayout worker_fragment_back;
    public RecyclerView recyclerView;
    Toolbar toolbar;
    CategoryAdapter categoryAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String currentUserID;

    List<WorkerViewModel> category_list = new ArrayList<>();

    int[] logos = {R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount, R.drawable.ic_discount};

    String[] names = {"Ramanna", "Raghunandhan", "Swamulu", "Somappa", "Thimmanna", "Prasanna", "Mallikarjun", "Koteswararao", "Anand", "Thayappa", "Sreekanth", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind", "Aravind"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_worker, container, false);

        recyclerView = root.findViewById(R.id.recyclerview);
        worker_fragment_back = root.findViewById(R.id.worker_fragment_background);

        fAuth = FirebaseAuth.getInstance();

        currentUserID = fAuth.getCurrentUser().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        FirestoreRecyclerOptions<WorkerViewModel> options =
                new FirestoreRecyclerOptions.Builder<WorkerViewModel>()
                        .setQuery(FirebaseFirestore.getInstance().collection("users"), WorkerViewModel.class)
                        .build();

        categoryAdapter = new CategoryAdapter(options);
        recyclerView.setAdapter(categoryAdapter);
        //worker_fragment_back.getBackground().setAlpha(200);
        return root;
    }
    @Override
    public void onStart(){
        super.onStart();
        categoryAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        categoryAdapter.stopListening();
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