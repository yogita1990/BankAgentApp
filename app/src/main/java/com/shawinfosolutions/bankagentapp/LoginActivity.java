package com.shawinfosolutions.bankagentapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shawinfosolutions.bankagentapp.Model.BankAgents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton, New_ExecButton;
    private Button submitBtn, ContinueBtn, SIgnInBtn;
    private String TAG = "LoginAsCust";
    private String mVerificationId = "";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private EditText editTextMobile, edtVerificatonCode;
    private ProgressDialog dialog;
    private String loading_message = "Please wait...";
    public static final String OTP_REGEX = "[0-9]{1,6}";
    private static final int REQUEST_ALL_PERMISSION = 200;
    private String token = "";
    private String MobileNo = "1233";
    private String StoreName = "", AgentName = "", AgentPhoto = "", AgentLoginStatus = "", AgentToken = "";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedpreferences;
    private String mobileNOVal;
    // private String agentId;
    private String ID, intentVal = "0";
    private ArrayList<String> mobilelist, IdList, StoreNameList, AgentNameList, AgentPhotoList, AgentLoginStatusList, AgentTokenList;
    private String agentToken;
    private LinearLayout LoginLayut;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        LoginButton = findViewById(R.id.LoginButton);
        New_ExecButton = findViewById(R.id.New_ExecButton);
        LoginLayut = findViewById(R.id.LoginLayut);
        LoginLayut.setVisibility(View.GONE);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginLayut.setVisibility(View.VISIBLE);
                New_ExecButton.setVisibility(View.GONE);
                LoginButton.setVisibility(View.GONE);
            }
        });
        New_ExecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("BankAgents");

        //  mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedpreferences = getSharedPreferences("Fashion", Context.MODE_PRIVATE);

        SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);
        StoreName = prefs.getString("StoreName", "");
        AgentName = prefs.getString("AgentName", "");
        AgentPhoto = prefs.getString("AgentPhoto", "");

        mobilelist = new ArrayList<>();
        mobilelist.clear();
        IdList = new ArrayList<>();
        IdList.clear();

        StoreNameList = new ArrayList<>();
        StoreNameList.clear();
        AgentNameList = new ArrayList<>();
        AgentNameList.clear();
        AgentPhotoList = new ArrayList<>();
        AgentPhotoList.clear();

        AgentLoginStatusList = new ArrayList<>();
        AgentLoginStatusList.clear();
        AgentTokenList = new ArrayList<>();
        AgentTokenList.clear();

        mAuth = FirebaseAuth.getInstance();
        editTextMobile = findViewById(R.id.editTextMobile);
        edtVerificatonCode = findViewById(R.id.edtVerificatonCode);
        submitBtn = findViewById(R.id.submitBtn);
        ContinueBtn = findViewById(R.id.ContinueBtn);
        SIgnInBtn = findViewById(R.id.SIgnInBtn);
        SIgnInBtn.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        edtVerificatonCode.setVisibility(View.GONE);
        editor = sharedpreferences.edit();
        Intent intent = getIntent();
        if (intent != null) {
            intentVal = intent.getStringExtra("intentVal");
        }
        Log.e("intentVal", "" + intentVal);
        agentToken = sharedpreferences.getString("agentToken", "");

        // String Mobile = prefs.getString("Mobile", "");
        //  agentId = prefs.getString("agentId", "");
        // Log.e("AgentId", "AgentId" + agentId);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken().toString();
                        Log.e("Tokenmsg", "TokenVal" + token);

                        //  ID = mDatabase.push().getKey().toString();
                        Log.e("IDVAL==", "" + ID);
                        Log.e("token==", "" + token);
                        //  Log.e("Mobie==", "" + editTextMobile.getText().toString());


                        Log.e("LoginAsSales", "AGentTokennn" + token);
                        editor.putString("AgentToken", token);
                        editor.commit();
                        editor.apply();


                    }
                });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            BankAgents agent = new BankAgents((String) userData.get("id"), (String) userData.get("branchName"), (String) userData.get("agentName")
                                    , (String) userData.get("agentmobileNo"), (String) userData.get("agentPhoto"),
                                    (String) userData.get("agentLoginStatus"), (String) userData.get("agentToken"));
                            if (
                                    (agent.getId() != null && !agent.getId().isEmpty() && !agent.getId().equals("null")) &&
                                            (agent.getBranchName() != null && !agent.getBranchName().isEmpty() && !agent.getBranchName().equals("null")) &&
                                            (agent.getAgentName() != null && !agent.getAgentName().isEmpty() && !agent.getAgentName().equals("null")) &&
                                            (agent.getAgentmobileNo() != null && !agent.getAgentmobileNo().isEmpty() && !agent.getAgentmobileNo().equals("null")) &&
                                            (agent.getAgentPhoto() != null && !agent.getAgentPhoto().isEmpty() && !agent.getAgentPhoto().equals("null")) &&
                                            (agent.getAgentLoginStatus() != null && !agent.getAgentLoginStatus().isEmpty() && !agent.getAgentLoginStatus().equals("null")) &&
                                            (agent.getAgentToken() != null && !agent.getAgentToken().isEmpty() && !agent.getAgentToken().equals("null"))
                            ) {

                                ID = agent.getId();
                                StoreName = agent.getBranchName();
                                AgentName = agent.getAgentName();
                                MobileNo = agent.getAgentmobileNo();
                                AgentPhoto = agent.getAgentPhoto();
                                AgentLoginStatus = agent.getAgentLoginStatus();
                                AgentToken = agentToken;
                                IdList.add(ID);
                                StoreNameList.add(StoreName);
                                AgentNameList.add(AgentName);
                                mobilelist.add(MobileNo);
                                AgentPhotoList.add(AgentPhoto);

                                AgentLoginStatusList.add(AgentLoginStatus);
                                AgentTokenList.add(AgentToken);


                                Log.e("StoreName", "StoreName==" + StoreName);
                                Log.e("MobileNo", "MobileNo==" + MobileNo);

                            }

//addTextToView(mUser.getName() + " - " + Integer.toString(mUser.getAge()));

                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
// If the object can’t be casted into HashMap, it means that it is of type String. 

                        }
                        //  editTextMobile.setText(MobileNo);

                    }
                    Log.e("mobilelistSize", "Size" + mobilelist.size());
                    Log.e("mobilelist", "Mobile==" + MobileNo);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNOVal = editTextMobile.getText().toString().trim();
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/" + mobileNOVal);

                if (mobileNOVal.isEmpty() || mobileNOVal.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/" + mobileNOVal);
                Log.e("intentVal", "" + intentVal);
                Log.e("IdList", "==" + IdList.size());

                try {
                    if (!(IdList.isEmpty())) {
                        Log.e("value", "Size" + mobilelist.contains(editTextMobile.getText().toString()));
                        Log.e("Position", "Size" + mobilelist.indexOf(mobileNOVal));
                        Log.e("Value", "Value" + mobilelist.get(0));

                        ID = IdList.get(mobilelist.indexOf(mobileNOVal));
                        StoreName = StoreNameList.get(mobilelist.indexOf(mobileNOVal));
                        AgentName = AgentNameList.get(mobilelist.indexOf(mobileNOVal));
                        AgentPhoto = AgentPhotoList.get(mobilelist.indexOf(mobileNOVal));
                        AgentLoginStatus = AgentLoginStatusList.get(mobilelist.indexOf(mobileNOVal));

                        editor.putString("ID", ID);


                        editor.putString("StoreName", StoreName);
                        editor.putString("AgentName", AgentName);
                        editor.putString("MobileNo", editTextMobile.getText().toString());
                        editor.putString("AgentPhoto", AgentPhoto);

                        editor.putString("AgentLoginStatus", AgentLoginStatus);
                        editor.commit();
                        editor.apply();

                        Log.e("ID", "ID===" + ID);
                        // Log.e("PARAM", "Size" + mobilelist.indexOf(mobileNOVal));


                        Log.i(TAG, "onTokenRefresh completed with token: " + token);

                        Log.e("mobilelistSize112", "Size" + mobilelist.size());

                        Log.e("MobileNoVal", "" + MobileNo);
                        if (intentVal.equalsIgnoreCase("0")) {
                            if (mobilelist.contains(editTextMobile.getText().toString())) {

                                Log.e("MobileNo", "" + MobileNo);

                                BankAgents agent = new BankAgents(ID, StoreName, AgentName, mobileNOVal, AgentPhoto,
                                        "true", AgentToken);
                                mDatabase.child(ID).setValue(agent);
                                edtVerificatonCode.setVisibility(View.GONE);


                                editor.putString("IsLoggedInSales", "1");


//                                editor.putString("MobileNo", editTextMobile.getText().toString());
//                                editor.putString("StoreName", StoreName);
//                                editor.putString("AgentName", AgentName);
//                                editor.putString("MobileNo", editTextMobile.getText().toString());
//                                editor.putString("AgentPhoto", AgentPhoto);
//                                editor.putString("Experties", Experties);
//                                editor.putString("Brand", Brand);
//                                editor.putString("AgentLoginStatus", "true");
//                                editor.putString("AgentToken", AgentToken);
                                editor.commit();
                                editor.apply();

                                SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);

                                String agentId = prefs.getString("ID", "");

                                Log.e("AgentId", "AgentId" + agentId);

                                Intent intent1 = new Intent(LoginActivity.this, OnlineCustomersListActivity.class);
                                intent1.putExtra("intentVal","1");

                                startActivity(intent1);
                                finish();
                            } else {
                                edtVerificatonCode.setVisibility(View.GONE);

                                Toast.makeText(LoginActivity.this, "You are not registered user.", Toast.LENGTH_SHORT).show();

                            }
                        } else if (mobilelist.contains(editTextMobile.getText().toString())) {
                            Log.e("MobileNo", "" + MobileNo);


                            BankAgents agent = new BankAgents(ID, StoreName, AgentName, mobileNOVal, AgentPhoto,
                                    "true", AgentToken);
                            mDatabase.child(ID).setValue(agent);
                            edtVerificatonCode.setVisibility(View.GONE);


                            editor.putString("IsLoggedInSales", "1");

                            editor.putString("MobileNo", editTextMobile.getText().toString());
                            editor.putString("StoreName", StoreName);
                            editor.putString("AgentName", AgentName);
                            editor.putString("MobileNo", editTextMobile.getText().toString());
                            editor.putString("AgentPhoto", AgentPhoto);

                            editor.putString("AgentLoginStatus", "true");
                            editor.putString("AgentToken", AgentToken);
                            editor.commit();
                            editor.apply();

                            Intent intent1 = new Intent(LoginActivity.this, OnlineCustomersListActivity.class);
                            intent1.putExtra("intentVal","1");

                            startActivity(intent1);
                            finish();

                        } else {
                            edtVerificatonCode.setVisibility(View.GONE);

                            Toast.makeText(LoginActivity.this, "You are not registered user.", Toast.LENGTH_SHORT).show();

                            // AsyncCallWS asyncetask = new AsyncCallWS(LoginActivity.this, "Please wait...", mobileNOVal);
                            // asyncetask.execute();
                        }

                    } else {
                        //  FirebaseMessaging.getInstance().subscribeToTopic("/topics/" + mobileNOVal);
                        Toast.makeText(LoginActivity.this, "You are not registered user.", Toast.LENGTH_SHORT).show();

//                        AsyncCallWS asyncetask = new AsyncCallWS(LoginActivity.this, "Please wait...", mobileNOVal);
//                        asyncetask.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        SIgnInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtVerificatonCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    edtVerificatonCode.setError("Enter valid code");
                    edtVerificatonCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        Context context;
        String str;
        String mobile;

        public AsyncCallWS(Context activity, String str, String mobile) {
            this.context = activity;
            this.str = str;
            this.mobile = mobile;

            // dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            if (dialog != null && !loading_message.equalsIgnoreCase("")) {
                dialog.setMessage(loading_message);
                dialog.setCancelable(true);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setProgress(0);
                dialog.setMax(100);

                dialog.show();
            }

        }


        @Override
        protected Void doInBackground(Void... voids) {
            sendVerificationCode(mobile);
            return null;
        }

        @Override

        protected void onPostExecute(Void aVoid) {
            try {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                SIgnInBtn.setVisibility(View.VISIBLE);
                ContinueBtn.setVisibility(View.GONE);

            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("Error", ex.getMessage());
                Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w("TAG", "getInstanceId failed", task.getException());
                                                return;
                                            }

                                            // Get new Instance ID token
                                            token = task.getResult().getToken().toString();
                                            Log.e("Tokenmsg", "TokenVal" + token);

                                            //  ID = mDatabase.push().getKey().toString();
                                            Log.e("IDVAL==", "" + ID);
                                            Log.e("token==", "" + token);
                                            //  Log.e("Mobie==", "" + editTextMobile.getText().toString());

                                            editor.putString("IsLoggedInSales", "1");
                                            editor.commit();
                                            editor.apply();
                                            // editor.remove("AgentToken").commit();

                                            editor.putString("StoreName", StoreName);
                                            editor.putString("AgentName", AgentName);
                                            editor.putString("MobileNo", editTextMobile.getText().toString());
                                            editor.putString("AgentPhoto", AgentPhoto);

                                            editor.putString("AgentLoginStatus", AgentLoginStatus);
                                            editor.putString("AgentToken", token);
                                            editor.commit();
                                            editor.apply();
                                            Log.e("LoginAsSales", "AGentTokennn" + token);

                                            BankAgents agent = new BankAgents(ID, StoreName, AgentName, mobileNOVal, AgentPhoto,
                                                    "true", token);
                                            // tokenn.setId(user.getUid());
                                            //  tokenn.setToken(token);
                                            // databaseref.child(id).setValue(tokenn);
                                            //  mDatabase.child("BankAgents").setValue(agent);
                                            mDatabase.child(ID).setValue(agent);
                                            // Toast.makeText(LoginActivity.this, "Artist added", Toast.LENGTH_LONG).show();
                                            //test


                                        }
                                    });
                            SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);

                            String agentId = prefs.getString("ID", "");

                            Log.e("AgentId", "AgentId" + agentId);
                            Intent intent = new Intent(LoginActivity.this, OnlineCustomersListActivity.class);
                            intent.putExtra("intentVal","1");

                            startActivity(intent);
                            finish();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Log.e("Error", "Error" + message);
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
/*
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Customer customer = dataSnapshot.getValue(Customer.class);
                // [START_EXCLUDE]
                Log.e("custDetails", "" + customer.getMobileNo());
                Log.e("custDetails111", "" + customer.getLoginStatus());
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(LoginActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
*/
    }


    //the callback to detect the verifi acation status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            Log.e("SMSCODE", "" + code);
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                edtVerificatonCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error111", "Error111" + e.getMessage());

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_ALL_PERMISSION:
//
//        }
    // }

}







