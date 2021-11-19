package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ArticlesCategories extends AppCompatActivity implements ArtcleCategoryAdapter.ArticlesList {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    FirebaseFirestore fStore;

    ArtcleCategoryAdapter artcleCategoryAdapter;

    List<ArticleCategoryViewModel> articleCategorylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_categories);

        recyclerView = findViewById(R.id.recyclerview_articles);
        //swipeRefreshLayout = findViewById(R.id.swipeRefreshArticles);

        fStore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        articleCategorylist.add(new ArticleCategoryViewModel("Trending", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Crops", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Fertilizers", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Pesticides", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Weather Report", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Harvesting", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Diseases", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Machines", R.drawable.ic_twotone_grass_24));
        articleCategorylist.add(new ArticleCategoryViewModel("Infield Works", R.drawable.ic_twotone_grass_24));

        artcleCategoryAdapter = new ArtcleCategoryAdapter(articleCategorylist, ArticlesCategories.this);
        recyclerView.setAdapter(artcleCategoryAdapter);
    }

    @Override
    public void articleList(ArticleCategoryViewModel articleCategoryViewModel) {
        startActivity(new Intent(this, ArticlesList.class).putExtra("data", articleCategoryViewModel));
    }
}