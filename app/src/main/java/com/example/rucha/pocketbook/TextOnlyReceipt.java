package com.example.rucha.pocketbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TextOnlyReceipt extends AppCompatActivity {
    TextInputEditText receiptName, receiptPrice, receiptStore, receiptComment;
    ImageView receiptImg;
    EditText purchaseDate;
    Spinner categorySpinner, paymentSpinner;
    Date dateObject;
    Calendar cal;
    Button setReminder;
    TextInputLayout layoutName, layoutPrice;
    int year_x,month_x,day_x;
    //dialog id for datepicker
    static final int DIALOG_ID = 0;
    private ArrayList<CategoryItem> categoryList;
    private CategoryAdapter categoryAdapter;

    private ArrayList<PaymentItem> paymentList;
    private PaymentAdapter paymentAdapter;

    DBHelper db;
    private Receipts receipt;
    InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_only_receipt);
        initViews();
        initCategoryList();
        initPaymentList();

        //create calendar instance
        cal = Calendar.getInstance();
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

        categoryAdapter = new CategoryAdapter(this, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        paymentAdapter = new PaymentAdapter(this, paymentList);
        paymentSpinner.setAdapter(paymentAdapter);

        db = new DBHelper(getApplicationContext());
        receipt = new Receipts();
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
        layoutName = (TextInputLayout)findViewById(R.id.textInputLayout_name);
        layoutPrice = (TextInputLayout)findViewById(R.id.textInputLayout_price);
    }
    private void initCategoryList(){
        categoryList = new ArrayList<CategoryItem>();
        categoryList.add(new CategoryItem(R.drawable.ic_category_others,getResources().getString(R.string.others)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_grocery, getResources().getString(R.string.grocery)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_garments, getResources().getString(R.string.garments)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_footwear, getResources().getString(R.string.footwear)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_accessories, getResources().getString(R.string.accessories)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_beauty, getResources().getString(R.string.beauty)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_education, getResources().getString(R.string.education)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_entertainment, getResources().getString(R.string.entertainment)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_health, getResources().getString(R.string.health)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_stationery, getResources().getString(R.string.stationery)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_hotel, getResources().getString(R.string.hotel)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_travel, getResources().getString(R.string.travel)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_trip, getResources().getString(R.string.trip)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_furnishing, getResources().getString(R.string.home)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_electronics, getResources().getString(R.string.electronics)));
        categoryList.add(new CategoryItem(R.drawable.ic_category_subscriptions, getResources().getString(R.string.subscriptions)));
    }

    private void initPaymentList(){
        paymentList = new ArrayList<PaymentItem>();
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_cash,getResources().getString(R.string.cash)));
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_credit_card, getResources().getString(R.string.credit)));
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_debit_card, getResources().getString(R.string.debit)));
        paymentList.add(new PaymentItem(R.drawable.ic_payment_method_e_wallets, getResources().getString(R.string.e_wallets)));
    }

    /**
     * DatePicker
     */
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID)
        {
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
            insertReceiptInDB();
            Intent receiptShow = new Intent(this, MainActivity.class);
            startActivity(receiptShow);
            finish();
        }
    }

    public void insertReceiptInDB(){
        receipt.setReceiptName(receiptName.getText().toString().trim());
        receipt.setPrice(Float.valueOf(receiptPrice.getText().toString().trim()));
        receipt.setReceiptStore(receiptStore.getText().toString().trim());
        receipt.setReceiptDate(dateObject);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateObject);
        int year = calendar.get(Calendar.YEAR);
        //Add one to month {0 - 11}
        int month = calendar.get(Calendar.MONTH) + 1;
        receipt.setYear(year);
        receipt.setMonth(month);

        receipt.setComment(receiptComment.getText().toString().trim());

        CategoryItem categoryItemSelected = (CategoryItem) categorySpinner.getSelectedItem();
        String selection = categoryItemSelected.getCategoryName().trim();
        int categoryID = db.getCategoryIDFromName(selection);
        receipt.setCategoryID(categoryID);

        PaymentItem paymentMethodSelected = (PaymentItem) paymentSpinner.getSelectedItem();
        selection = paymentMethodSelected.getPaymentMethod().trim();
        int paymentID = db.getPaymentMethodIDFromMethod(selection);
        receipt.setPaymentMethodID(paymentID);

        receipt.setReceiptType("TEXT ONLY");

        String email = SaveSharedPreferences.getEmail(getApplicationContext());
        int id = db.getUserIDFromEmail(email);
        receipt.setUserID(id);

        db.createReceipt(receipt);
        Toast.makeText(this,"Receipt added successfully",Toast.LENGTH_LONG).show();
    }
}

