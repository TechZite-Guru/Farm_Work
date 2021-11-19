package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ArticlesList extends AppCompatActivity implements ArticlesListAdapter.Article {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseFirestore fStore;
    ArticlesListAdapter articlesListAdapter;

    String category;

    List<ArticlesListViewModel> articles_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            ArticleCategoryViewModel articleCategoryViewModel = (ArticleCategoryViewModel) intent.getSerializableExtra("data");
            category = articleCategoryViewModel.getCategoryName();
        }

        setTitle(category);

        recyclerView = findViewById(R.id.recyclerview_articlesList);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshArticlesList);

        fStore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        collectData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articles_list.clear();
                collectData();
                articlesListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void collectData() {
        CollectionReference collectionReference = fStore.collection("Articles").document("DPfD1maa4wv7T89vgzIe").collection(category);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            ArticlesListViewModel articlesListViewModel = new ArticlesListViewModel(
                                    documentSnapshot.getString("ArticleTitle"),
                                    documentSnapshot.getString("ArticleContent"),
                                    documentSnapshot.getString("ArticleDate"),
                                    documentSnapshot.getString("ArticleImage")
                            );
                            articles_list.add(articlesListViewModel);
                        }
                    }

                    articlesListAdapter = new ArticlesListAdapter(articles_list, ArticlesList.this);
                    recyclerView.setAdapter(articlesListAdapter);
                }
                else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void article(ArticlesListViewModel articlesListViewModel) {
        startActivity(new Intent(this, Article.class).putExtra("data", articlesListViewModel));
    }
}