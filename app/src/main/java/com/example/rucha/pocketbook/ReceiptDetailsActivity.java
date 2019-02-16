package com.example.rucha.pocketbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptDetailsActivity extends AppCompatActivity {
    int id, userId;
    String name, category, paymentMethod, store, comment, imagePath, receiptType;
    Date date;
    Float price;
    byte[] byteArray;
    LinearLayout parentLinearLayout;
    TextView txtName, txtPrice, txtDate, txtCategory, txtPaymentMethod;
    ImageView image;
    FloatingActionButton edit, delete;
    DBHelper db;
    Context ctx;
    Bitmap bmp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);
        db = new DBHelper(this);
        ctx = ReceiptDetailsActivity.this;

        intent = getIntent();
        id = intent.getIntExtra("id",0);
        userId = intent.getIntExtra("userid",0);
        name = intent.getStringExtra("name");
        price = intent.getFloatExtra("price",0.0f);
        date = (Date)intent.getSerializableExtra("date");
        category = intent.getStringExtra("category");
        paymentMethod = intent.getStringExtra("paymentBy");
        store = intent.getStringExtra("store");
        comment = intent.getStringExtra("comment");
        receiptType = intent.getStringExtra("type");

//        byteArray = getIntent().getByteArrayExtra("image");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


        parentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout);
        txtName = (TextView)findViewById(R.id.receipt_details_name);
        txtPrice = (TextView)findViewById(R.id.receipt_details_price);
        txtDate = (TextView)findViewById(R.id.receipt_details_date);
        txtCategory = (TextView)findViewById(R.id.receipt_details_category);
        txtPaymentMethod = (TextView)findViewById(R.id.receipt_details_payment_method);
        image = (ImageView)findViewById(R.id.receipt_details_image);
        edit = (FloatingActionButton) findViewById(R.id.receipt_details_edit_fab);
        delete = (FloatingActionButton)findViewById(R.id.receipt_details_delete_fab);

        txtName.setText(name);
        txtPrice.setText(String.valueOf(price));

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(date);
        txtDate.setText(reportDate);
        txtCategory.setText(category);
        txtPaymentMethod.setText(paymentMethod);

        if(receiptType.equalsIgnoreCase("WITH PICTURE")) {
            imagePath =intent.getStringExtra("imagePath");
            bmp = BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(bmp);
        }
        else if(receiptType.equalsIgnoreCase("TEXT ONLY")){
            image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_logo));
        }
        image.setAdjustViewBounds(true);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(store != null && !store.equalsIgnoreCase("")){
            View rowView = inflater.inflate(R.layout.receipt_details_extra_item, null);
            TextView label = (TextView)rowView.findViewById(R.id.receipt_details_item_label);
            TextView data = (TextView)rowView.findViewById(R.id.receipt_details_item);
            label.setText("Store");
            data.setText(store);
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
        }

        if(comment != null && !comment.equalsIgnoreCase("")){
            View rowView = inflater.inflate(R.layout.receipt_details_extra_item, null);
            TextView label = (TextView)rowView.findViewById(R.id.receipt_details_item_label);
            TextView data = (TextView)rowView.findViewById(R.id.receipt_details_item);
            Log.d("comment found", comment);
            label.setText("Comment");
            data.setText(comment);
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Receipts receipts = new Receipts();
                receipts.setID(id);
                receipts.setUserID(userId);
                receipts.setReceiptName(name);
                receipts.setPrice(price);
                receipts.setReceiptDate(date);
                receipts.setReceiptStore(store);
                receipts.setComment(comment);
                receipts.setRImage(imagePath);
                receipts.setReceiptType(receiptType);

                DBHelper db = new DBHelper(ctx);
                int cid = db.getCategoryIDFromName(category);
                receipts.setCategoryID(cid);

                int pid = db.getPaymentMethodIDFromMethod(paymentMethod);
                receipts.setPaymentMethodID(pid);

                Intent intent = new Intent(ctx, UpdateReceipt.class);
                intent.putExtra("Receipts object", receipts);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ctx, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ctx);
                }
                builder.setTitle("Delete Receipt")
                        .setMessage("Are you sure you want to delete this receipt?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteReceipt(id);
                                Toast.makeText(ctx,"Receipt deleted successfully",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ctx, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }
}
