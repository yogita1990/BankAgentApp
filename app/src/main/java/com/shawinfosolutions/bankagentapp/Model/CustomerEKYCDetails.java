package com.shawinfosolutions.bankagentapp.Model;

public class CustomerEKYCDetails {

    private String custDetails,mobileNo;

    public CustomerEKYCDetails() {
    }

    public CustomerEKYCDetails( String mobileNo,String custDetails) {
        this.custDetails = custDetails;
        this.mobileNo = mobileNo;
    }

    public String getCustDetails() {
        return custDetails;
    }

    public void setCustDetails(String custDetails) {
        this.custDetails = custDetails;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
