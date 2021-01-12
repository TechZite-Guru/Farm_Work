package com.example.farmwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<com.example.farmwork.CategoryAdapter.CategoryAdapterVh> {

    private List<CategoryModel> categoryModelList;
    private List<CategoryModel> getCategoryModelListFiltered;
    private Context context;
    public SelectedCategory hiCategory;

    public CategoryAdapter(List<CategoryModel> categoryModelList, SelectedCategory selectedCategory) {
        this.categoryModelList = categoryModelList;
        this.hiCategory = selectedCategory;
        this.getCategoryModelListFiltered = categoryModelList;
    }

    @NonNull
    @Override
    public com.example.farmwork.CategoryAdapter.CategoryAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CategoryAdapterVh(LayoutInflater.from(context).inflate(R.layout.available_workers,null));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapterVh holder, int position) {

        CategoryModel categoryModel = categoryModelList.get(position);
        int prefix = categoryModel.getPrefix();
        String category_name = categoryModel.getCategory();
        int category_row = categoryModel.getCategory_row();

        holder.tvprefix.setImageResource(prefix);
        holder.tvcategory_name.setText(category_name);

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public interface SelectedCategory {

        void selectedCategory(CategoryModel categoryModel);
    }

    public class CategoryAdapterVh extends RecyclerView.ViewHolder {
        ImageView tvprefix;
        TextView tvcategory_name;
        LinearLayout tvcategory_row;
        public CategoryAdapterVh(@NonNull View itemView) {
            super(itemView);
            tvprefix = itemView.findViewById(R.id.prefix);
            tvcategory_name = itemView.findViewById(R.id.category_name);
            tvcategory_row = itemView.findViewById(R.id.category_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hiCategory.selectedCategory(categoryModelList.get(getAdapterPosition()));
                }
            });

        }
    }
}
