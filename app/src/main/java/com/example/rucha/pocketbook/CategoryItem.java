package com.example.rucha.pocketbook;

/**
   A class for one item of category spinner
 **/
public class CategoryItem {
    private String mCategoryName;
    private int mCategoryImage;

    public CategoryItem(int categoryImage, String categoryName){
       mCategoryName = categoryName;
       mCategoryImage = categoryImage;
    }

    public String getCategoryName(){
        return mCategoryName;
    }

    public int getCategoryImage(){
        return mCategoryImage;
    }
}
