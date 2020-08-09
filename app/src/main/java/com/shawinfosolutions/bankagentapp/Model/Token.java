package com.shawinfosolutions.bankagentapp.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Token {
    public String token;

    public String mobileNo;
    public String id;

    public Token() {

    }

    public Token(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Token(String id, String mobileNo, String token) {
        this.id = id;
        this.mobileNo = mobileNo;
        this.token = token;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
/*
    public Token(String token) {
        this.token = token;
    }
*/


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

}
