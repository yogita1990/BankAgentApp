package com.shawinfosolutions.bankagentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shawinfosolutions.bankagentapp.Model.CustomerEKYCDetails;
import com.shawinfosolutions.bankagentapp.Model.PaymentDetails;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfPaymentActivity  extends AppCompatActivity {
    private RecyclerView payment_list;
    private String mobileNoVal, MessageBody, key2;
    ArrayList<String> payList;
    private PaymentListAdapter paymentListAdapter;
    private ArrayList<String> MobileNoList,PaymentList;
    private ActionBar actionbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_payment_layout);
        actionbar = getSupportActionBar();
        actionbar.setTitle("Customer History");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        payment_list = findViewById(R.id.payment_list);
        payment_list.setLayoutManager(new LinearLayoutManager(this));

        payList=new ArrayList<>();
        MobileNoList=new ArrayList<>();
        PaymentList=new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("CustomerEKYCDetails").child("");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MobileNoList.clear();
                PaymentList.clear();
                if (dataSnapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            CustomerEKYCDetails paymentDetails = new CustomerEKYCDetails((String) userData.get("mobileNo"),(String)userData.get("custDetails"));
                            if (
                                    (paymentDetails.getMobileNo() != null && !paymentDetails.getMobileNo().isEmpty() && !paymentDetails.getMobileNo().equals("null")) &&

                                            (paymentDetails.getCustDetails() != null && !paymentDetails.getCustDetails().isEmpty() && !paymentDetails.getCustDetails().equals("null"))

                            ) {

                                String  paydetails = paymentDetails.getCustDetails();
                                String MobileNoVal = paymentDetails.getMobileNo();
                                PaymentList.add(paydetails);
                                MobileNoList.add(MobileNoVal);

                                Log.e("paydetails", "Size" + paydetails);
                                Log.e("MobileNoVal", "Mobile==" + MobileNoVal);


                            }


                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
// If the object can’t be casted into HashMap, it means that it is of type String. 

                        }
                        //  editTextMobile.setText(MobileNo);

                    }



                    paymentListAdapter = new PaymentListAdapter(ListOfPaymentActivity.this, PaymentList,MobileNoList);
                    payment_list.setAdapter(paymentListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent122 = new Intent(ListOfPaymentActivity.this, OnlineCustomersListActivity.class);
                startActivity(intent122);

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListOfPaymentActivity.this, OnlineCustomersListActivity.class);
        startActivity(intent);
        finish();
    }
}

