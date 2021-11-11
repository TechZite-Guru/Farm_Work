package com.example.farmwork;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MachineryFragment extends Fragment {

    LinearLayout cuttersBtn, harvestersBtn, plantingBtn, sprayersBtn, shreddersBtn, tillersBtn, seedingBtn, tractorsBtn, weedingBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_machinery, container, false);
        setHasOptionsMenu(true);

        Log.d("SUCCESS", "In Machinery Fragment");

        cuttersBtn = root.findViewById(R.id.cuttersBtn);
        harvestersBtn = root.findViewById(R.id.harvesterBtn);
        plantingBtn = root.findViewById(R.id.plantingBtn);
        sprayersBtn = root.findViewById(R.id.sprayersBtn);
        shreddersBtn = root.findViewById(R.id.shreddersBtn);
        tillersBtn = root.findViewById(R.id.tillersBtn);
        seedingBtn = root.findViewById(R.id.seedingBtn);
        tractorsBtn = root.findViewById(R.id.tractorBtn);
        weedingBtn = root.findViewById(R.id.weedingBtn);

        cuttersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Cutters");
            }
        });

        harvestersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Harvester");
            }
        });

        plantingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Planting");
            }
        });

        sprayersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Sprayers");
            }
        });

        shreddersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Shredders");
            }
        });

        tillersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Tillers");
            }
        });

        seedingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Seeding");
            }
        });

        tractorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Tractors");
            }
        });

        weedingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Weeding");
            }
        });


        return root;
    }

    public void openCategory(String category) {
        Intent to_shops = new Intent(getContext(), EquipmentShops.class);
        to_shops.putExtra("category", category);
        startActivity(to_shops);
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
                    //categoryAdapter.getFilter().filter(s);
                    searchView.setQuery("", false);
                    searchView.setIconifiedByDefault(true);
                }
                Toast.makeText(getContext(), "Came Out", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    //categoryAdapter.getFilter().filter(newText);
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