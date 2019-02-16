package com.example.rucha.pocketbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ReceiptCardAdapter extends RecyclerView.Adapter<ReceiptCardAdapter.MyHolder>{
    List<ReceiptCard> receiptCards;
    Context mContext;
    String type;
    ReceiptCard receiptCard;

    public ReceiptCardAdapter(Context context, List<ReceiptCard> receiptCards){
        this.receiptCards = receiptCards;
        mContext = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt_card,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        receiptCard = receiptCards.get(position);
        holder.receiptTitle.setText(receiptCard.getReceiptCardTitle());
        holder.receiptPrice.setText(String.valueOf(receiptCard.getReceiptCardPrice()));
        holder.receiptCategory.setText(receiptCard.getReceiptCardCategory());
        type = receiptCard.getReceiptCardType();
        Log.e("type",type);
        if(type.equalsIgnoreCase("WITH PICTURE"))
        {
            holder.receiptImage.setImageBitmap(receiptCard.getReceiptCardImage());
        }
        else if(type.equalsIgnoreCase("TEXT ONLY"))
        {
            holder.receiptImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_logo));
        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReceiptDetailsActivity.class);
                receiptCard = receiptCards.get(position);
                intent.putExtra("id",receiptCard.getReceiptCardId());
                intent.putExtra("name",receiptCard.getReceiptCardTitle());
                intent.putExtra("price",receiptCard.getReceiptCardPrice());
                intent.putExtra("store",receiptCard.getReceiptCardStore());
                intent.putExtra("date",receiptCard.getReceiptCardDate());
                intent.putExtra("category",receiptCard.getReceiptCardCategory());
                intent.putExtra("paymentBy",receiptCard.getReceiptCardPaymentMethod());
                intent.putExtra("comment",receiptCard.getReceiptCardComment());
                intent.putExtra("userid",receiptCard.getReceiptCardUserID());
                intent.putExtra("type",receiptCard.getReceiptCardType());
                //convert image to byte array
//                Bitmap image = receiptCard.getReceiptCardImage();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                //sending byte array as intent
//                intent.putExtra("image",byteArray);
                    intent.putExtra("imagePath", receiptCard.getReceiptCardImagePath());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiptCards.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView receiptImage;
        TextView receiptTitle, receiptCategory, receiptPrice;
        ConstraintLayout parentLayout;
        public MyHolder(View itemView){
            super(itemView);
            receiptImage = (ImageView) itemView.findViewById(R.id.card_image);
            receiptTitle = (TextView) itemView.findViewById(R.id.title);
            receiptCategory = (TextView) itemView.findViewById(R.id.card_category);
            receiptPrice = (TextView) itemView.findViewById(R.id.receipt_item_price);
            parentLayout = (ConstraintLayout) itemView.findViewById(R.id.receipt_card_parent_layout);
        }
    }
}
