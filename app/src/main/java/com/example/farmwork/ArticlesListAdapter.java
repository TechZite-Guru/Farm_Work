package com.example.farmwork;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ArticlesListAdapterVh> {

    private List<ArticlesListViewModel> articlesListViewModelList;
    com.example.farmwork.ArticlesList articlesList;
    Article article;


    public ArticlesListAdapter(List<ArticlesListViewModel> articleListViewModelList, com.example.farmwork.ArticlesList articlesList) {
        this.articlesListViewModelList = articleListViewModelList;
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public ArticlesListAdapter.ArticlesListAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articlelistitem, parent, false);
        return new ArticlesListAdapter.ArticlesListAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesListAdapter.ArticlesListAdapterVh holder, int position) {
        ArticlesListViewModel articlesListViewModel = articlesListViewModelList.get(position);
        holder.articleTitle.setText(articlesListViewModel.getArticleTitle());
        holder.articleContent.setText(articlesListViewModel.getArticleContent());
        holder.articleDate.setText(articlesListViewModel.getArticleDate());
        Picasso.get().load(articlesListViewModel.getArticleImage()).placeholder(R.drawable.ic_twotone_grass_24).into(holder.articleImg);
    }

    @Override
    public int getItemCount() {
        return articlesListViewModelList.size();
    }

    public interface Article {
        void article(ArticlesListViewModel articlesListViewModel);
    }

    public class ArticlesListAdapterVh extends RecyclerView.ViewHolder {

        TextView articleTitle, articleContent, articleDate;
        ImageView articleImg;
        LinearLayout articleLayout;

        public ArticlesListAdapterVh(@NonNull View itemView) {
            super(itemView);

            articleTitle = itemView.findViewById(R.id.article_title);
            articleContent = itemView.findViewById(R.id.article_content);
            articleDate = itemView.findViewById(R.id.article_date);
            articleImg = itemView.findViewById(R.id.article_img);
            articleLayout = itemView.findViewById(R.id.article_layout);

            articleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    articlesList.article(articlesListViewModelList.get(getAdapterPosition()));
                }
            });
        }
    }
}
