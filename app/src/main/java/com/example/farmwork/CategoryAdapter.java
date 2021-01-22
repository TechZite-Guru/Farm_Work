package com.example.farmwork;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterVh> implements Filterable {

    private List<WorkerViewModel> workerViewModelList;
    private List<WorkerViewModel> getWorkerViewModelListFiltered;
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

        Picasso.get().load(workerViewModel.getProfile_image()).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.tvprefix);
        holder.tvname.setText(workerViewModel.getName());
        holder.tvlocation.setText(workerViewModel.getLocation());

        /*holder.tvbook_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_booking_page = new Intent(holder.tvbook_now_btn.getContext(), BookingPage.class);
                to_booking_page.putExtra("name", category_name);
                to_booking_page.putExtra("location", location);
                to_booking_page.putExtra("profile_image", prefix);

                to_booking_page.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.tvbook_now_btn.getContext().startActivity(to_booking_page);
            }
        });*/

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

                if (charSequence == null | charSequence.length() == 0){
                    filterResults.count = getWorkerViewModelListFiltered.size();
                    filterResults.values = getWorkerViewModelListFiltered;

                }else {
                    String searchChr = charSequence.toString().toLowerCase();
                    List<WorkerViewModel> resultData = new ArrayList<>();
                    //List<Sub_WorkerViewModel> resultData_sub = new ArrayList<>();

                    for (WorkerViewModel categoryModel: getWorkerViewModelListFiltered){
                        if (categoryModel.getName().toLowerCase().contains(searchChr)) {
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
        Button tvbook_now_btn;
        ImageView tvprefix;
        TextView tvname, tvlocation;
        CardView tvcardView;
        LinearLayout tvcategory_name;
        LinearLayout tvcategory_row;
        public CategoryAdapterVh(@NonNull View itemView) {
            super(itemView);
            tvbook_now_btn = itemView.findViewById(R.id.book_now_btn);
            tvprefix = itemView.findViewById(R.id.prefix);
            tvname = itemView.findViewById(R.id.name);
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
