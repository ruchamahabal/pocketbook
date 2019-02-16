package com.example.rucha.pocketbook;

public class PaymentItem {
    private String mPaymentMethod;
    private int mPaymentImage;

    public PaymentItem(int paymentImage, String paymentMethod){
        mPaymentImage = paymentImage;
        mPaymentMethod = paymentMethod;
    }

    public String getPaymentMethod(){
        return mPaymentMethod;
    }

    public int getPaymentImage(){
        return mPaymentImage;
    }

}
