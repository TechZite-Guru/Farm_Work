package com.example.farmwork;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    private int prefix;
    private String category;
    private int category_row;

    public CategoryModel(int prefix, String category){
        this.prefix = prefix;
        this.category = category;
    }

    public int getPrefix(){
        return prefix;
    }

    public String getCategory() {
        return category;
    }

    public int getCategory_row() { return category_row; };
}
