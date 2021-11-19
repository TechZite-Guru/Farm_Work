package com.example.farmwork;

import java.io.Serializable;

public class ArticleCategoryViewModel implements Serializable {
    String categoryName;
    int categoryImg;

    ArticleCategoryViewModel(String categoryName, int categoryImg) {
        this.categoryName = categoryName;
        this.categoryImg = categoryImg;
    }

    public String getCategoryName() { return categoryName; }

    public int getCategoryImg() { return categoryImg; }
}
