package com.example.farmwork;

import java.io.Serializable;

public class ArticlesListViewModel implements Serializable {
    String articleTitle, articleContent, articleDate;
    String articleImage;

    ArticlesListViewModel(String articleTitle, String articleContent, String articleDate, String articleImage) {
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleDate = articleDate;
        this.articleImage = articleImage;
    }

    public String getArticleTitle() { return articleTitle; }

    public String getArticleContent() { return  articleContent; }

    public String getArticleDate() { return articleDate; }

    public String getArticleImage() { return articleImage; }
}
