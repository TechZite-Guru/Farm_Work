package com.example.farmwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Article extends AppCompatActivity {

    TextView title, content, date;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        title = findViewById(R.id.a_title);
        content = findViewById(R.id.a_content);
        date = findViewById(R.id.a_date);
        imageView = findViewById(R.id.a_img);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            ArticlesListViewModel articlesListViewModel = (ArticlesListViewModel) intent.getSerializableExtra("data");
            date.setText(articlesListViewModel.getArticleDate());
            title.setText(articlesListViewModel.getArticleTitle());
            content.setText(articlesListViewModel.getArticleContent());
            Picasso.get().load(articlesListViewModel.getArticleImage()).placeholder(R.drawable.ic_twotone_grass_24).into(imageView);
        }
    }
}