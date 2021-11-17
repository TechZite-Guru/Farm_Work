package com.example.farmwork;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpertsAdapter extends RecyclerView.Adapter<ExpertsAdapter.ExpertsAdapterVh> {

    private List<ExpertsViewModel> expertsViewModelList;
    private List<ExpertsViewModel> getExpertsViewModelList;
    ExpertsPage expertsPage;
    Context context;

    public ExpertsAdapter(List<ExpertsViewModel> expertsViewModelList, ExpertsPage expertsPage) {
        this.expertsPage = expertsPage;
        this.expertsViewModelList = expertsViewModelList;
    }

    @NonNull
    @Override
    public ExpertsAdapter.ExpertsAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopslist, parent, false);
        return new ExpertsAdapter.ExpertsAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertsAdapter.ExpertsAdapterVh holder, int position) {
        ExpertsViewModel expertsViewModel = expertsViewModelList.get(position);

        holder.expertName.setText(expertsViewModel.getExpertName());
        holder.expertRole.setText(expertsViewModel.getExpertRole());
        holder.available.setText(expertsViewModel.getAvailability());
        holder.languages.setText(expertsViewModel.getLanguage1()+", "+expertsViewModel.getLanguage2()+", "+expertsViewModel.getLanguage3());
        holder.expertCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", expertsViewModel.getPhoneNumber(), null));
                v.getContext().startActivity(intentCall);
            }
        });

    }

    @Override
    public int getItemCount() {
        return expertsViewModelList.size();
    }


    public class ExpertsAdapterVh extends RecyclerView.ViewHolder {
        TextView expertName, expertRole, available, textavailbe, languages;
        LinearLayout linearLayout;
        Button expertCall;

        public ExpertsAdapterVh(@NonNull View itemview) {
            super(itemview);
            expertName = itemview.findViewById(R.id.shopName);
            expertRole = itemview.findViewById(R.id.shopAddress);
            available = itemview.findViewById(R.id.available);
            textavailbe = itemview.findViewById(R.id.textavailable);
            textavailbe.setVisibility(View.GONE);
            languages = itemview.findViewById(R.id.languagesKnown);
            linearLayout = itemview.findViewById(R.id.linear_layout_shop);

            expertCall = itemview.findViewById(R.id.expertcallBtn);

        }
    }
}
