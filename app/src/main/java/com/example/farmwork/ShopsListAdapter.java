package com.example.farmwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShopsListAdapter extends RecyclerView.Adapter<ShopsListAdapter.ShopsListAdapterVh> {

    private List<ShopsViewModel> shopsViewModelList;
    private List<ShopsViewModel> getShopsViewModelList;
    EquipmentShops equipmentShops;
    ShopPage shopPage;
    Context context;

    public ShopsListAdapter(List<ShopsViewModel> shopsViewModelList, EquipmentShops equipmentShops) {
        this.equipmentShops = equipmentShops;
        this.shopsViewModelList = shopsViewModelList;
    }

    @NonNull
    @Override
    public ShopsListAdapter.ShopsListAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopslist, parent, false);
        return new ShopsListAdapter.ShopsListAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsListAdapter.ShopsListAdapterVh holder, int position) {
        ShopsViewModel shopsViewModel = shopsViewModelList.get(position);

        holder.shopName.setText(shopsViewModel.getShopName());
        holder.shopAddress.setText(shopsViewModel.getShopStreet()+", "+shopsViewModel.getShopVillage()+", "+shopsViewModel.getShopTown()+", "+shopsViewModel.getShopState());
        holder.available.setText(shopsViewModel.getAvailable_count());

    }

    @Override
    public int getItemCount() {
        return shopsViewModelList.size();
    }

    public interface ShopPage {
        void shopPage(ShopsViewModel shopsViewModel);
    }

    public class ShopsListAdapterVh extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress, available, languages;
        RelativeLayout linearLayout;
        Button call;

        public ShopsListAdapterVh(@NonNull View itemview) {
            super(itemview);
            shopName = itemview.findViewById(R.id.shopName);
            shopAddress = itemview.findViewById(R.id.shopAddress);
            available = itemview.findViewById(R.id.available);
            linearLayout = itemview.findViewById(R.id.relative_layout_shop);
            languages = itemview.findViewById(R.id.languagesKnown);
            languages.setVisibility(View.GONE);

            call = itemview.findViewById(R.id.expertcallBtn);
            call.setVisibility(View.GONE);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    equipmentShops.shopPage(shopsViewModelList.get(getAdapterPosition()));
                }
            });
        }
    }
}