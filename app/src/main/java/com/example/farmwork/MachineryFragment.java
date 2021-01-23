package com.example.farmwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MachineryFragment extends Fragment {

    private MachineryViewModel machineryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        machineryViewModel =
                new ViewModelProvider(this).get(MachineryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_machinery, container, false);
        setHasOptionsMenu(true);
        final TextView textView = root.findViewById(R.id.text_notifications);
        machineryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
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

        return super.onOptionsItemSelected(item);
    }
}