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

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<com.example.farmwork.CategoryAdapter.CategoryAdapterVh> {

    private List<WorkerViewModel> categoryModelList;
    private List<WorkerViewModel> getHomeViewModelListFiltered;
    private Context context;

    public CategoryAdapter(List<WorkerViewModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public com.example.farmwork.CategoryAdapter.CategoryAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CategoryAdapterVh(LayoutInflater.from(context).inflate(R.layout.available_workers,null));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapterVh holder, int position) {

        WorkerViewModel categoryModel = categoryModelList.get(position);
        int prefix = categoryModel.getPrefix();
        String category_name = categoryModel.getCategory();
        int category_row = categoryModel.getCategory_row();

        holder.tvprefix.setImageResource(prefix);
        holder.tvname.setText(category_name);

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }


    public class CategoryAdapterVh extends RecyclerView.ViewHolder {
        ImageView tvprefix;
        TextView tvname;
        CardView tvcardView;
        LinearLayout tvcategory_name;
        LinearLayout tvcategory_row;
        public CategoryAdapterVh(@NonNull View itemView) {
            super(itemView);
            tvprefix = itemView.findViewById(R.id.prefix);
            tvname = itemView.findViewById(R.id.name);
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
