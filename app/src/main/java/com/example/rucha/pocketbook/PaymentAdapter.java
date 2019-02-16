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

import com.example.rucha.pocketbook.PaymentItem;

import java.util.ArrayList;

public class PaymentAdapter extends ArrayAdapter<PaymentItem> {
      public PaymentAdapter(Context context, ArrayList<PaymentItem> paymentList){
          super(context,0, paymentList);
      }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
          if(convertView == null){
              convertView = LayoutInflater.from(getContext()).inflate(R.layout.payment_spinner_item,parent,false);
          }
          ImageView imageView = (ImageView) convertView.findViewById(R.id.payment_method_image);
        TextView textView = (TextView) convertView.findViewById(R.id.payment_method);

        PaymentItem currentItem = getItem(position);
        if(currentItem != null){
            imageView.setImageResource(currentItem.getPaymentImage());
            textView.setText(currentItem.getPaymentMethod());
        }
        return convertView;
    }
}
