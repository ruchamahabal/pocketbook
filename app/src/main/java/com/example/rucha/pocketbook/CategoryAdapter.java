package com.example.rucha.pocketbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rucha.pocketbook.CategoryItem;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryList){
        super(context,0, categoryList);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        //if the view is not inflated, then inflate
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_item,parent,false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.category_type_image);
        TextView textView = (TextView) convertView.findViewById(R.id.category_type);

        CategoryItem currentItem = getItem(position);

        if(currentItem != null) {
            imageView.setImageResource(currentItem.getCategoryImage());
            textView.setText(currentItem.getCategoryName());
        }
        return convertView;
    }
}
