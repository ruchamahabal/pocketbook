package com.example.rucha.pocketbook;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateReceipt extends AppCompatActivity {
    TextInputEditText receiptName, receiptPrice, receiptStore, receiptComment;
    ImageView receiptImg;
    Bitmap bitmap;
    EditText purchaseDate;
    Spinner categorySpinner, paymentSpinner;
    Date dateObject;
    Calendar cal;
    TextInputLayout layoutName, layoutPrice;
    Button btnChange;
    String type;
    int year_x,month_x,day_x;
    int id, userId;
    String imagePath;
    //dialog id for datepicker
    static final int DIALOG_ID = 0;
    private ArrayList<CategoryItem> categoryList;
    private CategoryAdapter categoryAdapter;
    private List<String> categoryStrings;

    private ArrayList<PaymentItem> paymentList;
    private PaymentAdapter paymentAdapter;
    private List<String> paymentMStrings;

    DBHelper db;
    private Receipts receipt, receipts;
    InputValidation inputValidation;
    Context context;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final int PICK_IMAGE_FROM_GALLERY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_receipt);
        context = UpdateReceipt.this;
        db = new DBHelper(getApplicationContext());
        receipt = new Receipts();
        initViews();
        initCategoryList();
        initPaymentList();
        setViewsFromIntent();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose source");

                // add a list
                String[] options = {"Choose From Gallery", "Open Camera"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery();
                                break;
                            case 1:
                                openCamera();
                                break;
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_receipt_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()) {
            case R.id.action_done:
                verifyInputFilled();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * Initialize the views
     */
    private void initViews(){
        inputValidation = new InputValidation(this);
        receiptName = (TextInputEditText) findViewById(R.id.input_name);
        receiptPrice = (TextInputEditText) findViewById(R.id.input_price);
        receiptStore = (TextInputEditText) findViewById(R.id.input_store);
        purchaseDate = (EditText) findViewById(R.id.input_date);
        categorySpinner = (Spinner)findViewById(R.id.spinner_input_category);
        paymentSpinner = (Spinner) findViewById(R.id.spinner_input_payment_method);
        receiptComment = (TextInputEditText)findViewById(R.id.input_comment);
        receiptImg = (ImageView) findViewById(R.id.receipt_image);
        layoutName = (TextInputLayout)findViewById(R.id.textInputLayout_name);
        layoutPrice = (TextInputLayout)findViewById(R.id.textInputLayout_price);
        btnChange = (Button)findViewById(R.id.btn_change_img);
    }

    /**
     * Initialize the category list for category spinner
     */
    private void initCategoryList(){
        categoryList = new ArrayList<CategoryItem>();
        categoryList.add(new CategoryItem(R.drawable.ic_category_others,getResources().getString(R.string.others)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_accessories, getResources().getString(R.string.accessories)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_beauty, getResources().getString(R.string.beauty)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_education, getResources().getString(R.string.education)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_electronics, getResources().getString(R.string.electronics)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_entertainment, getResources().getString(R.string.entertainment)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_footwear, getResources().getString(R.string.footwear)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_garments, getResources().getString(R.string.garments)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_grocery, getResources().getString(R.string.grocery)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_health, getResources().getString(R.string.health)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_furnishing, getResources().getString(R.string.home)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_hotel, getResources().getString(R.string.hotel)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_stationery, getResources().getString(R.string.stationery)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_subscriptions, getResources().getString(R.string.subscriptions)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_travel, getResources().getString(R.string.travel)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_trip, getResources().getString(R.string.trip)));

        categoryStrings = Arrays.asList(getResources().getStringArray(R.array.categories));

    }

    /**
     * Initialize the payment method list for payment method spinner
     */
    private void initPaymentList(){
        paymentList = new ArrayList<PaymentItem>();
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_cash,getResources().getString(R.string.cash)));
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_credit_card, getResources().getString(R.string.credit)));
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_debit_card, getResources().getString(R.string.debit)));
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_e_wallets, getResources().getString(R.string.e_wallets)));

        paymentMStrings = Arrays.asList(getResources().getStringArray(R.array.paymentMethods));
    }

    /**
     * sets views as per previous values
     */
    private void setViewsFromIntent() {

        Intent intent = getIntent();
        receipts = (Receipts) intent.getSerializableExtra("Receipts object");

        //fetching attributes
        id = receipts.getID();
        userId = receipts.getUserID();
        int cid = receipts.getCategoryID();
        int pid = receipts.getPaymentMethodID();
        String name = receipts.getReceiptName();
        float price = receipts.getPrice();
        String store = receipts.getReceiptStore();
        String comment = receipts.getComment();
        Date dt = receipts.getReceiptDate();
        type = receipts.getReceiptType();
        //byte[] img = receipts.getRImage();
        //Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
        //receiptImg.setImageBitmap(bmp);
        imagePath = receipts.getRImage();
        if(type.equalsIgnoreCase("WITH PICTURE")) {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath);
            receiptImg.setImageBitmap(bmp);
        }

        else if(type.equalsIgnoreCase("TEXT ONLY")){
            receiptImg.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_logo));
        }

        //setting views
        receiptName.setText(name);
        receiptPrice.setText(String.valueOf(price));
        receiptStore.setText(store);
        receiptComment.setText(comment);

        //create calendar instance
        cal = Calendar.getInstance();
        cal.setTime(dt);
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        purchaseDate.setText(day_x+"/"+(month_x+1)+"/"+year_x);
        dateObject = new Date(year_x - 1900, month_x, day_x);
        purchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //inbuilt method
                showDialog(DIALOG_ID);
            }
        });

        //getting the category from id, getting position and setting spinner to already selected position
        String category = db.getCategoryNameFromID(cid);
        Log.e("category_update_R", category);
        int spinnerPosition = categoryStrings.indexOf(category);
        Log.e("cur",String.valueOf(spinnerPosition));
        //set the default according to value
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(spinnerPosition,true);

        //getting the category from id, getting position and setting spinner to already selected position
        String paymentM = db.getPaymentMethodFromID(pid);
        spinnerPosition = paymentMStrings.indexOf(paymentM);
        //set the default according to value
        paymentAdapter = new PaymentAdapter(this, paymentList);
        paymentSpinner.setAdapter(paymentAdapter);
        paymentSpinner.setSelection(spinnerPosition, true);
    }

    /**
     * DatePicker
     */
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID){
            DatePickerDialog dialog = new DatePickerDialog(this, datePickListener, year_x, month_x, day_x);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            year_x = year;
            month_x = month;
            day_x = day;
            purchaseDate.setText(day_x+"/"+(month_x+1)+"/"+year_x);
            //creating date object from the date selected from DatePicker
            dateObject = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
        }
    };

    /**
     * open camera for taking receipt picture
     */
    public void openCamera(){
        //intent for capturing image to camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * open gallery for importing an image
     */
    public void openGallery(){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_FROM_GALLERY);
    }

    /**
     * method to do the action after camera and gallery intents return results
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        type = "WITH PICTURE";
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //inserting image to gallery
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "image", null);
                //converting bitmap to uri so that large images can also be sent in the intent
                Uri imageUri = Uri.parse(path);
                Log.e("URI",imageUri.toString());
                try {
                    imagePath = getFilePath(this, imageUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                receiptImg.setImageBitmap(bitmap);
                int rotateImage = getCameraPhotoOrientation(this, imageUri, imagePath);
                receiptImg.setRotation(rotateImage);
            }
            if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data!= null) {
                Uri imageUri = data.getData();
                //String path = getRealPathFromURI(imageUri);
                // String path = imageUri.getPath();
                try {
                    imagePath = getFilePath(this,imageUri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                receiptImg.setImageBitmap(bitmap);
                int rotateImage = getCameraPhotoOrientation(this, imageUri, imagePath);
                receiptImg.setRotation(rotateImage);
            }
        }
    }

    /**
     * method for getting image path
     */

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * capture image orientation
     */
    public int getCameraPhotoOrientation(Context context, Uri imageUri,
                                         String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    /**
     * Method to verify whether required fields are filled up using InputValidation class
     */
    private void verifyInputFilled(){
        if (!inputValidation.isInputEditTextFilled(receiptName, layoutName, getString(R.string.error_message_product_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(receiptPrice, layoutPrice, getString(R.string.error_message_product_price))) {
            return;
        }
        else{
            updateReceiptInDB();
            Intent receiptShow = new Intent(this, MainActivity.class);
            startActivity(receiptShow);
            finish();
        }
    }

    public void updateReceiptInDB(){
        receipt.setReceiptName(receiptName.getText().toString().trim());
        receipt.setPrice(Float.valueOf(receiptPrice.getText().toString().trim()));
        receipt.setReceiptStore(receiptStore.getText().toString().trim());
        receipt.setReceiptDate(dateObject);

        //converting bitmap to byte array
//        Bitmap image=((BitmapDrawable)receiptImg.getDrawable()).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] imageByteArray = stream.toByteArray();
        receipt.setRImage(imagePath);
        receipt.setReceiptType(type);

        receipt.setComment(receiptComment.getText().toString().trim());

        CategoryItem categoryItemSelected = (CategoryItem) categorySpinner.getSelectedItem();
        String selection = categoryItemSelected.getCategoryName().trim();
        int categoryID = db.getCategoryIDFromName(selection);
        receipt.setCategoryID(categoryID);

        PaymentItem paymentMethodSelected = (PaymentItem) paymentSpinner.getSelectedItem();
        selection = paymentMethodSelected.getPaymentMethod().trim();
        int paymentID = db.getPaymentMethodIDFromMethod(selection);
        receipt.setPaymentMethodID(paymentID);
        receipt.setReceiptType(type);

        receipt.setUserID(userId);
        receipt.setID(id);

        db.updateReceipt(receipt);
        Toast.makeText(this,"Receipt updated successfully",Toast.LENGTH_LONG).show();
    }
}
