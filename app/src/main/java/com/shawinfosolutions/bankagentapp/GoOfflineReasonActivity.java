package com.shawinfosolutions.bankagentapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GoOfflineReasonActivity  extends AppCompatActivity {
    private Button submitBtn;
    private ActionBar actionbar;
    private String Reason="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.go_offline_reason_layout);
        actionbar = getSupportActionBar();
        actionbar.setTitle("Reasons for Go Offline");

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        submitBtn=findViewById(R.id.submitBtn);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.groupradio);
       // radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(GoOfflineReasonActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                 Reason= (String) rb.getText();
                 Intent intent=new Intent(GoOfflineReasonActivity.this,OnlineCustomersListActivity.class);
                 startActivity(intent);
               // Toast.makeText(GoOfflineReasonActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(GoOfflineReasonActivity.this, OnlineCustomersListActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GoOfflineReasonActivity.this, OnlineCustomersListActivity.class);
        startActivity(intent);
        finish();
    }
}

