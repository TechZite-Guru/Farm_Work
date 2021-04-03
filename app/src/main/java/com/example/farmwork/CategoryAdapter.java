package com.example.farmwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterVh> implements Filterable {

    private List<WorkerViewModel> workerViewModelList;
    private List<WorkerViewModel> locationList;
    private final List<WorkerViewModel> getWorkerViewModelListFiltered;
    BookingPage bookingPage;
    WorkerFragment workerFragment;

    public CategoryAdapter(List<WorkerViewModel> workerViewModelList, WorkerFragment workerFragment) {
        this.workerViewModelList = workerViewModelList;
        this.workerFragment = workerFragment;
        this.getWorkerViewModelListFiltered = workerViewModelList;
    }


    @NonNull
    @Override
    public CategoryAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_workers, parent, false);
        return new CategoryAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterVh holder, int position) {

        WorkerViewModel workerViewModel = workerViewModelList.get(position);

        Picasso.get().load(workerViewModel.getProfile_image()).placeholder(R.drawable.profile_image_placeholder).into(holder.tvprefix);
        holder.tvname.setText(workerViewModel.getName());
        holder.tvlocation.setText(workerViewModel.getLocality()+", "+workerViewModel.getAdminarea());
        holder.tvworker_fare.setText(workerViewModel.getFare()+"/-"+"  PER DAY");

        double myLatitude = workerViewModel.getMylatitude();
        double myLongitude = workerViewModel.getMylongitude();

        double lat1 = workerViewModel.getLatitude();
        double long1 = workerViewModel.getLongitude();

        if (lat1 != 0 && long1 != 0) {

            double longDiff = myLongitude - long1;

            double distance = Math.sin(deg2rad(myLatitude))
                    * Math.sin(deg2rad(lat1))
                    + Math.cos(deg2rad(myLatitude))
                    * Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(longDiff));

            distance = Math.acos(distance);

            distance = rad2deg(distance);

            distance = distance * 60 * 1.1515;

            distance = distance * 1.609344;

            Log.d("Latitude","" +lat1);
            Log.d("Longitude","" +long1);

            Log.d("MyLatitude","" +myLatitude);
            Log.d("MyLongitude","" +myLongitude);


            holder.tvdistance.setText(String.format("%2f KM", distance));
        }
    }

    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double myLatitude) {
        return (myLatitude*Math.PI/180.0);
    }

    @Override
    public int getItemCount() {
        return workerViewModelList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if (charSequence == null || charSequence.length() == 0){
                    filterResults.count = getWorkerViewModelListFiltered.size();
                    filterResults.values = getWorkerViewModelListFiltered;

                }else {
                    String searchChr = charSequence.toString().toLowerCase();
                    List<WorkerViewModel> resultData = new ArrayList<>();
                    //List<WorkerViewModel> resultData_sub = new ArrayList<>();

                    for (WorkerViewModel categoryModel: getWorkerViewModelListFiltered){
                        if ((categoryModel.getName().toLowerCase().contains(searchChr)) || (categoryModel.getLocality().toLowerCase().contains(searchChr))) {
                            resultData.add(categoryModel);
                        }
                    }
                    filterResults.count = getWorkerViewModelListFiltered.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                workerViewModelList = (List<WorkerViewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface BookingPage {
        void bookingPage(WorkerViewModel workerViewModel);
    }


    public class CategoryAdapterVh extends RecyclerView.ViewHolder {
        View view;
        Button tvbook_now_btn, tvworker_fare;
        ImageView tvprefix;
        TextView tvname, tvlocation, tvdistance;
        CardView tvcardView;
        LinearLayout tvcategory_name;
        LinearLayout tvcategory_row;
        public CategoryAdapterVh(@NonNull View itemView) {
            super(itemView);
            tvbook_now_btn = itemView.findViewById(R.id.book_now_btn);
            tvprefix = itemView.findViewById(R.id.prefix);
            tvname = itemView.findViewById(R.id.name);
            tvdistance = itemView.findViewById(R.id.distance);
            tvworker_fare = itemView.findViewById(R.id.worker_fare_amount);
            tvlocation = itemView.findViewById(R.id.location);
            tvcategory_row = itemView.findViewById(R.id.category_row);
            tvcardView = itemView.findViewById(R.id.cardView);
            view = itemView;

            tvbook_now_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    workerFragment.bookingPage(workerViewModelList.get(getAdapterPosition()));
                }
            });

        }
    }

    /*public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }*/
}
