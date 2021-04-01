package com.example.farmwork;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkerBookedAdapter extends RecyclerView.Adapter<WorkerBookedAdapter.WorkerBookerAdapterVh> {

    private List<WorkerBookedModel> workerBookedModelList;
    private List<WorkerBookedModel> getWorkerBookedModelList;
    WorkerBookedHistory workerBookedHistory;
    Context context;

    public WorkerBookedAdapter(List<WorkerBookedModel> workerBookedModelList, WorkerBookedHistory workerBookedHistory) {
        this.workerBookedHistory = workerBookedHistory;
        this.workerBookedModelList = workerBookedModelList;
    }

    @NonNull
    @Override
    public WorkerBookedAdapter.WorkerBookerAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_booked_layout, parent, false);
        return new WorkerBookedAdapter.WorkerBookerAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerBookedAdapter.WorkerBookerAdapterVh holder, int position) {
        WorkerBookedModel workerBookedModel = workerBookedModelList.get(position);
        Log.d("Date", ""+workerBookedModel.getDate());

        holder.tvName.setText(String.format("%s  %s", workerBookedModel.getBoo_name(), workerBookedModel.getName()));
        holder.tvPhone.setText(String.format("%s  %s", workerBookedModel.getBoo_phone(), workerBookedModel.getPhone()));
        holder.tvVillage.setText(String.format("%s  %s", workerBookedModel.getBoo_village(), workerBookedModel.getVillage()));
        holder.tvDate.setText(String.format("%s  %s", workerBookedModel.getBoo_date(), workerBookedModel.getDate()));

    }

    @Override
    public int getItemCount() {
        return workerBookedModelList.size();
    }

    public class WorkerBookerAdapterVh extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvVillage, tvDate;
        public WorkerBookerAdapterVh(@NonNull View itemview) {
            super(itemview);
            tvName = itemview.findViewById(R.id.booker_name);
            tvPhone = itemview.findViewById(R.id.booker_phone);
            tvVillage = itemview.findViewById(R.id.booker_village);
            tvDate = itemview.findViewById(R.id.book_date);

        }
    }
}
