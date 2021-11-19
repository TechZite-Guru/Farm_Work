package com.example.farmwork;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArtcleCategoryAdapter extends RecyclerView.Adapter<ArtcleCategoryAdapter.ArtcleCategoryAdapterVh> {

    private List<ArticleCategoryViewModel> articleCategoryViewModelList;
    ArticlesCategories articlesCategories;
    ArticlesList articlesList;

    public ArtcleCategoryAdapter(List<ArticleCategoryViewModel> articleCategoryViewModelList, ArticlesCategories articlesCategories) {
        this.articleCategoryViewModelList = articleCategoryViewModelList;
        this.articlesCategories = articlesCategories;
    }

    @NonNull
    @Override
    public ArtcleCategoryAdapter.ArtcleCategoryAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articlecategorylistitem, parent, false);
        return new ArtcleCategoryAdapter.ArtcleCategoryAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtcleCategoryAdapter.ArtcleCategoryAdapterVh holder, int position) {
        ArticleCategoryViewModel articleCategoryViewModel = articleCategoryViewModelList.get(position);
        holder.articleCategoryName.setText(articleCategoryViewModel.getCategoryName());
        holder.articleCategoryImg.setImageResource(articleCategoryViewModel.getCategoryImg());

    }

    @Override
    public int getItemCount() {
        return articleCategoryViewModelList.size();
    }

    public interface ArticlesList {
        void articleList(ArticleCategoryViewModel articleCategoryModel);
    }

    public class ArtcleCategoryAdapterVh extends RecyclerView.ViewHolder {

        TextView articleCategoryName;
        ImageView articleCategoryImg;
        LinearLayout articleLayout;

        public ArtcleCategoryAdapterVh(@NonNull View itemView) {
            super(itemView);

            articleCategoryName = itemView.findViewById(R.id.articleCategoryName);
            articleCategoryImg = itemView.findViewById(R.id.art_category_img);
            articleLayout = itemView.findViewById(R.id.linearlayout_article);

            articleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    articlesCategories.articleList(articleCategoryViewModelList.get(getAdapterPosition()));
                }
            });
        }
    }
}
