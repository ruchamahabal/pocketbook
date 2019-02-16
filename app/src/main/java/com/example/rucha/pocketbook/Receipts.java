package com.example.rucha.pocketbook;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class Receipts implements Serializable {
    public static final String TAG = "Receipts";

    private int id;
    private String name;
    private float price;
    private Date date;
    private int year;
    private String receiptType;
    private int month;
    private String store;
    private String image;
    private String comment;
    private int userID;
    private int categoryID;
    private int paymentMethodID;

    public Receipts(){
    }

    public Receipts(int id, String name, float price, Date date, String store, String Image, String comment, int userID, int categoryID, int paymentMethodID)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.store = store;
        this.image = Image;
        this.comment = comment;
        this.categoryID = categoryID;
        this.paymentMethodID = paymentMethodID;
        this.userID = userID;
    }
    public int getID(){
        return id;
    }
    public void setID(int id){
        this.id = id;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptName(){
        return name;
    }
    public void setReceiptName(String name){
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    public float getPrice(){
        return price;
    }
    public void setPrice(float price){
        this.price = price;
    }
    public Date getReceiptDate(){
        return date;
    }
    public void setReceiptDate(Date date){
        this.date = date;
    }
    public String getReceiptStore(){
        return store;
    }
    public void setReceiptStore(String store){
        this.store = store;
    }
    public String getRImage(){
        return image;
    }
    public void setRImage(String image){
        this.image = image;
    }
    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public void setCategoryID(int categoryID){
        this.categoryID = categoryID;
    }
    public int getPaymentMethodID(){
        return paymentMethodID;
    }
    public void setPaymentMethodID(int paymentMethodID){
        this.paymentMethodID = paymentMethodID;
    }
}
