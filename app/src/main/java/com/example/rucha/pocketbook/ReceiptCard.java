package com.example.rucha.pocketbook;

import android.graphics.Bitmap;

import java.util.Date;

public class ReceiptCard {
    private int receiptCardId;
    private String receiptCardTitle;
    private float receiptCardPrice;
    private String receiptCardStore;
    private Date receiptCardDate;
    private String receiptCardCategory;
    private String receiptCardPaymentMethod;
    private Bitmap receiptCardImage;
    private String receiptCardType;
    private String receiptCardImagePath;
    private String receiptCardComment;
    private int receiptCardUserID;

    public ReceiptCard(){

    }
    public ReceiptCard(int id, String title, float price, String category, Bitmap image){
        receiptCardId = id;
        receiptCardTitle = title;
        receiptCardPrice = price;
        receiptCardCategory = category;
        receiptCardImage = image;
    }

    public int getReceiptCardId() {
        return receiptCardId;
    }

    public void setReceiptCardId(int receiptCardId) {
        this.receiptCardId = receiptCardId;
    }

    public String getReceiptCardTitle() {
        return receiptCardTitle;
    }

    public void setReceiptCardTitle(String receiptCardTitle) {
        this.receiptCardTitle = receiptCardTitle;
    }

    public float getReceiptCardPrice() {
        return receiptCardPrice;
    }

    public void setReceiptCardPrice(float receiptCardPrice) {
        this.receiptCardPrice = receiptCardPrice;
    }

    public String getReceiptCardCategory() {
        return receiptCardCategory;
    }

    public void setReceiptCardCategory(String receiptCardCategory) {
        this.receiptCardCategory = receiptCardCategory;
    }

    public Bitmap getReceiptCardImage() {
        return receiptCardImage;
    }

    public void setReceiptCardImage(Bitmap receiptCardImage) {
        this.receiptCardImage = receiptCardImage;
    }

    public String getReceiptCardType() {
        return receiptCardType;
    }

    public void setReceiptCardType(String receiptCardType) {
        this.receiptCardType = receiptCardType;
    }

    public String getReceiptCardStore() {
        return receiptCardStore;
    }

    public void setReceiptCardStore(String receiptCardStore) {
        this.receiptCardStore = receiptCardStore;
    }

    public Date getReceiptCardDate() {
        return receiptCardDate;
    }

    public void setReceiptCardDate(Date receiptCardDate) {
        this.receiptCardDate = receiptCardDate;
    }

    public String getReceiptCardPaymentMethod() {
        return receiptCardPaymentMethod;
    }

    public void setReceiptCardPaymentMethod(String receiptCardPaymentMethod) {
        this.receiptCardPaymentMethod = receiptCardPaymentMethod;
    }

    public String getReceiptCardComment() {
        return receiptCardComment;
    }

    public void setReceiptCardComment(String receiptCardComment) {
        this.receiptCardComment = receiptCardComment;
    }

    public int getReceiptCardUserID() {
        return receiptCardUserID;
    }

    public void setReceiptCardUserID(int receiptCardUserID) {
        this.receiptCardUserID = receiptCardUserID;
    }

    public String getReceiptCardImagePath() {
        return receiptCardImagePath;
    }

    public void setReceiptCardImagePath(String receiptCardImagePath) {
        this.receiptCardImagePath = receiptCardImagePath;
    }

}
