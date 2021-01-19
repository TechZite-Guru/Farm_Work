package com.example.farmwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends FirestoreRecyclerAdapter<WorkerViewModel, CategoryAdapter.CategoryAdapterVh> {

    private List<WorkerViewModel> categoryModelList;
    private List<WorkerViewModel> getHomeViewModelListFiltered;
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<WorkerViewModel> options) {
        super(options);
    }


    @NonNull
    @Override
    public CategoryAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_workers, parent, false);
        return new CategoryAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterVh holder, int position, @NonNull WorkerViewModel model) {

        String prefix = model.getProfile_image();
        String category_name = model.getName();
        String location = model.getLocation();
        int category_row = model.getCategory_row();
        Picasso.get().load(model.getProfile_image()).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.tvprefix);
        holder.tvname.setText(category_name);
        holder.tvlocation.setText(location);

    }


    public class CategoryAdapterVh extends RecyclerView.ViewHolder {
        ImageView tvprefix;
        TextView tvname, tvlocation;
        CardView tvcardView;
        LinearLayout tvcategory_name;
        LinearLayout tvcategory_row;
        public CategoryAdapterVh(@NonNull View itemView) {
            super(itemView);
            tvprefix = itemView.findViewById(R.id.prefix);
            tvname = itemView.findViewById(R.id.name);
            tvlocation = itemView.findViewById(R.id.location);
            tvcategory_row = itemView.findViewById(R.id.category_row);
            tvcardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }
}
