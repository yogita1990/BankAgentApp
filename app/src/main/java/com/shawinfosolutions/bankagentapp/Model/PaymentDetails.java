package com.shawinfosolutions.bankagentapp.Model;

public class PaymentDetails {


    private String paymentDetails;
    private String mobileNo;

    public PaymentDetails() {
    }

    public PaymentDetails(String paymentDetails, String mobileNo) {
        this.paymentDetails = paymentDetails;
        this.mobileNo = mobileNo;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
