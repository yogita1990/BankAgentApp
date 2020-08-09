package com.shawinfosolutions.bankagentapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.json.DupDetector;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.shawinfosolutions.bankagentapp.Model.BankAgents;
import com.shawinfosolutions.bankagentapp.Model.MeetingData;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class OnlineCustomersListActivity extends AppCompatActivity {
    private TextView onlineCustTxt;
    private Button goOfflineBtn, goOnlineBtn, onlineBtn;
    private ActionBar actionbar;
    private AlertDialog alertDialog;
    private TextView paymentTxt;
    private Button attendwBtn;
    private String MessageBody = "";
    private String MobileNo = "";
    private String StoreName = "", AgentName = "", AgentPhoto = "", AgentLoginStatus = "", AgentToken = "";
    private DatabaseReference mDatabase;
    private String agentId;
    private String mobileNoVal;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String key2;
    private ImageView PhotoImg;
    private SharedPreferences prefs;
    // private ProgressBar mProgressBar;
    private EditText firstname, lastname, email, aadharNo, mobile;

    private static final int REQUEST_ALL_PERMISSION = 200;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri filePathURI;
    private StorageTask mUploadTask;
    private Uri mImageUri;
    private Uri downloadUri = null;
    private String downloadUriStr = "";
    private LinearLayout imageCamLayout;
    private LinearLayout OnlineLayout;
    private ImageView PhotoImgVal;
    private LinearLayout OfflineModeLayout;
    private ArrayList<String> custList, MeetingIdList, MobileNoList;
    private RecyclerView cust_list;
    private CustListAdapter agentListAdapter;
    private Button payNowBtn;
    final String TAG = "NOTIFICATION TAG";
    private TextView qtyTxt;
    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private String TOPIC;
    private String ImageValue;
    private String idKey;
    private Button submitBtn;
    private String[] permissions = {


            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_cust_list);
        actionbar = getSupportActionBar();
        actionbar.setTitle("Online Customer List");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        custList = new ArrayList<>();
        MeetingIdList = new ArrayList<>();
        MobileNoList = new ArrayList<>();

        OfflineModeLayout = findViewById(R.id.OfflineModeLayout);
        OfflineModeLayout.setVisibility(View.VISIBLE);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        aadharNo = findViewById(R.id.aadharNo);
        mobile = findViewById(R.id.mobile);
        PhotoImgVal = findViewById(R.id.PhotoImgVal);
        PhotoImgVal.setVisibility(View.GONE);
        attendwBtn = findViewById(R.id.attendwBtn);
        PhotoImg = findViewById(R.id.PhotoImg);
        cust_list = findViewById(R.id.cust_list);
        payNowBtn = findViewById(R.id.payNowBtn);
        paymentTxt = findViewById(R.id.paymentTxt);
        qtyTxt = findViewById(R.id.qtyTxt);
        cust_list.setLayoutManager(new LinearLayoutManager(this));
        submitBtn = findViewById(R.id.submitBtn);
        OnlineLayout = findViewById(R.id.OnlineLayout);
        OnlineLayout.setVisibility(View.GONE);
        OfflineModeLayout.setVisibility(View.VISIBLE);


//Test
//        OnlineLayout.setVisibility(View.VISIBLE);
//        OfflineModeLayout.setVisibility(View.GONE);

        imageCamLayout = findViewById(R.id.imageCamLayout);
        onlineCustTxt = findViewById(R.id.onlineCustTxt);
        onlineCustTxt.setText("Online Customers " + "(0)");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ALL_PERMISSION);
        } else {

        }
        DatabaseReference mDatabaseMeeting = FirebaseDatabase.getInstance().getReference("MeetingData");

        mDatabaseMeeting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MobileNoList.clear();
                MeetingIdList.clear();
                if (dataSnapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            MeetingData meetingData = new MeetingData((String) userData.get("meetingId"), (String) userData.get("mobileNo"), (String) userData.get("dateTime"));
                            if (
                                    (meetingData.getMeetingId() != null && !meetingData.getMeetingId().isEmpty() && !meetingData.getMeetingId().equals("null")) &&
                                            (meetingData.getDateTime() != null && !meetingData.getDateTime().isEmpty() && !meetingData.getDateTime().equals("null")) &&
                                            (meetingData.getMobileNo() != null && !meetingData.getMobileNo().isEmpty() && !meetingData.getMobileNo().equals("null"))

                            ) {

                                String MeetingId = meetingData.getMeetingId();
                                String MobileNoVal = meetingData.getMobileNo();
                                MeetingIdList.add(MeetingId);
                                MobileNoList.add(MobileNoVal);

                                if (MobileNoList.isEmpty()) {
                                    // onlineCustTxt.setVisibility(View.INVISIBLE);
                                    onlineCustTxt.setText("Online Customers " + "(0)");

                                } else {
                                    onlineCustTxt.setText("Online Customers " + "(" + MobileNoList.size() + ")");

                                }

                                Log.e("StoreName", "StoreName==" + StoreName);
                                Log.e("MobileNo", "MobileNo==" + MobileNo);

                            }
                            if (MobileNoList.isEmpty()) {
                                // onlineCustTxt.setVisibility(View.INVISIBLE);
                                onlineCustTxt.setText("Online Customers " + "(0)");

                            } else {
                                onlineCustTxt.setText("Online Customers " + "(" + MobileNoList.size() + ")");

                            }


//addTextToView(mUser.getName() + " - " + Integer.toString(mUser.getAge()));

                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
// If the object can’t be casted into HashMap, it means that it is of type String. 

                        }
                        //  editTextMobile.setText(MobileNo);

                    }
//                    Log.e("mobilelistSize", "Size" + mobilelist.size());
//                    Log.e("mobilelist", "Mobile==" + MobileNo);
                    Collections.reverse(MeetingIdList);
                    Collections.reverse(MobileNoList);
//                    Collections.sort(myList, new Comparator<MyObject>() {
//                        public int compare(MyObject o1, MyObject o2) {
//                            if (o1.getDateTime() == null || o2.getDateTime() == null)
//                                return 0;
//                            return o1.getDateTime().compareTo(o2.getDateTime());
//                        }
//                    });
                    if (MobileNoList.isEmpty()) {
                        // onlineCustTxt.setVisibility(View.INVISIBLE);
                        onlineCustTxt.setText("Online Customers " + "(0)");

                    } else {
                        onlineCustTxt.setText("Online Customers " + "(" + MobileNoList.size() + ")");

                    }
                    Log.e("MobileNoList", "===" + MobileNoList.size());
                    agentListAdapter = new CustListAdapter(OnlineCustomersListActivity.this, MeetingIdList, MobileNoList);
                    cust_list.setAdapter(agentListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateData()) {
                    //Yogita

                    takeScreenShot();

                    if (ValidateScreenShot()) {
                        Log.e("Hiii", "1111");


                        String FinalBillValue = "Your aadhar varification  done successfully. ";
                        //   TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                        TOPIC = "/topics/" + key2; //topic has to match what the receiver subscribed to
                        NOTIFICATION_TITLE = "Aadhar Verification";
                        NOTIFICATION_MESSAGE = FinalBillValue;


                        JSONObject notification = new JSONObject();
                        JSONObject notifcationBody = new JSONObject();
                        try {
                            notifcationBody.put("title", NOTIFICATION_TITLE);
                            notifcationBody.put("message", NOTIFICATION_MESSAGE);
                            notifcationBody.put("key1", "user");
                            notifcationBody.put("key2", downloadUriStr);
                            notifcationBody.put("key3", key2);

                            notification.put("to", TOPIC);
                            notification.put("data", notifcationBody);
                            Log.e(TAG, "notification: " + notification);

                        } catch (JSONException e) {
                            Log.e(TAG, "onCreate: " + e.getMessage());
                        }

                        sendNotifications(notification);

                    }
                    //if(ds)
                }
            }
        });

        imageCamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });
        goOfflineBtn = findViewById(R.id.goOfflineBtn);
        goOnlineBtn = findViewById(R.id.goOnlineBtn);
        onlineCustTxt = findViewById(R.id.onlineCustTxt);
        //TakeAPicturewBtn=findViewById(R.id.TakeAPicturewBtn);

        mDatabase = FirebaseDatabase.getInstance().getReference("BankAgents");
        prefs = getSharedPreferences("Fashion", MODE_PRIVATE);
        StoreName = prefs.getString("StoreName", "");
        AgentName = prefs.getString("AgentName", "");
        AgentPhoto = prefs.getString("AgentPhoto", "");

        MobileNo = prefs.getString("MobileNo", "");
        AgentLoginStatus = prefs.getString("AgentLoginStatus", "");
        AgentToken = prefs.getString("AgentToken", "");
        Log.e("OnlineCust", "AGentToken" + AgentToken);
        sharedpreferences = getSharedPreferences("Fashion", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        mStorageRef = FirebaseStorage.getInstance().getReference().child("ImageData");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ImageData");
        URL serverURL;
        try {
            //serverURL = new URL("https://meet.jit.si");//test
            serverURL = new URL("https://meet.ezycom.co.in/");
            //https://meet.ezycom.co.in/
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

/*
        TakeAPicturewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            }
        });
*/
        agentId = prefs.getString("ID", "");
        key2 = prefs.getString("key2", "");
        Log.e("AgentId", "AgentId" + agentId);
       /* Agent agent = new Agent(agentId,StoreName, AgentName, MobileNo, AgentPhoto, Experties, Brand, "false", AgentToken);
        // tokenn.setId(user.getUid());
        //  tokenn.setToken(token);
        // databaseref.child(id).setValue(tokenn);
        // mDatabase.child("BankAgents").setValue(agent);
        mDatabase.child(agentId).setValue(agent);*/
        Log.e("MessageBody", "MessageBody:" + MessageBody);
        try {
            if (getIntent().getExtras() != null) {
                MessageBody = getIntent().getExtras().getString("MessageBody");

                if (!(MessageBody.equalsIgnoreCase(""))) {
                    OnlineLayout.setVisibility(View.VISIBLE);
                    OfflineModeLayout.setVisibility(View.GONE);
                    //Bundle extras = getIntent().getExtras();
                    String MessageTitle = getIntent().getExtras().getString("MessageTitle");
                    mobileNoVal = getIntent().getExtras().getString("mobileNoVal");
                    key2 = getIntent().getExtras().getString("key2");
                    Log.e("MessageTitle", "" + MessageTitle);
                    Log.e("MessageBody", "" + MessageBody);
                    Log.e("mobileNoVal", "" + mobileNoVal);
                    Log.e("key2", "" + key2);

                    custList.add(MessageBody);

                    editor.remove("key2").clear().commit();
                    editor.putString("key2", key2);
                    editor.putString("mobileNoVal", mobileNoVal);
                    editor.commit();
                    editor.apply();


                    if (MessageBody.length() > 0) {
               /* agentId = prefs.getString("ID", "");

                Log.e("AgentId","AgentId"+agentId);
                 agent = new Agent(agentId,StoreName, AgentName, MobileNo, AgentPhoto, Experties, Brand, "false", AgentToken);
                // tokenn.setId(user.getUid());
                //  tokenn.setToken(token);
                // databaseref.child(id).setValue(tokenn);
                // mDatabase.child("BankAgents").setValue(agent);
                mDatabase.child(agentId).setValue(agent);
                Log.e("MessageBody", "MessageBody:" + MessageBody);
*/
                        //  Build options object for joining the conference. The SDK will merge the default
                        // one we set earlier and this one when joining.
                        JitsiMeetConferenceOptions options
                                = new JitsiMeetConferenceOptions.Builder()
                                .setRoom(MessageBody.trim())
                                .build();
                        //   custList.remove(MessageBody);
                        // Launch the new activity with the given options. The launch() method takes care
                        // of creating the required Intent and passing the options.
                        JitsiMeetActivity.launch(OnlineCustomersListActivity.this, options);


                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String LoginStatus = sharedpreferences.getString("LoginStatus", null);
        if (LoginStatus != null) {
            if (LoginStatus.equalsIgnoreCase("true")) {
                goOnlineBtn.setVisibility(View.GONE);
                goOfflineBtn.setVisibility(View.VISIBLE);

            } else {
                goOnlineBtn.setVisibility(View.VISIBLE);
                goOfflineBtn.setVisibility(View.GONE);

            }
        }

        goOnlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(OnlineCustomersListActivity.this);
                dialog.setContentView(R.layout.custom_online);
                Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
                // if button is clicked, close the custom dialog
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        agentId = prefs.getString("ID", "");
                        goOnlineBtn.setVisibility(View.GONE);
                        goOfflineBtn.setVisibility(View.VISIBLE);
                        Log.e("AgentId", "AgentId" + agentId);
                        editor.putString("LoginStatus", "true");
                        editor.commit();
                        editor.apply();
                        BankAgents agent = new BankAgents(agentId, StoreName, AgentName, MobileNo, AgentPhoto, "true", AgentToken);
                        // tokenn.setId(user.getUid());
                        //  tokenn.setToken(token);
                        // databaseref.child(id).setValue(tokenn);
                        // mDatabase.child("BankAgents").setValue(agent);
                        mDatabase.child(agentId).setValue(agent);

                        //  Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
        goOfflineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOnlineBtn.setVisibility(View.VISIBLE);
                goOfflineBtn.setVisibility(View.GONE);

                editor.putString("LoginStatus", "false");
                editor.commit();
                editor.apply();
                Log.e("AgentId", "AgentId" + agentId);
                BankAgents agent = new BankAgents(agentId, StoreName, AgentName, MobileNo, AgentPhoto, "false", AgentToken);
                // tokenn.setId(user.getUid());
                //  tokenn.setToken(token);
                // databaseref.child(id).setValue(tokenn);
                // mDatabase.child("BankAgents").setValue(agent);
                mDatabase.child(agentId).setValue(agent);
                Intent intent = new Intent(OnlineCustomersListActivity.this, GoOfflineReasonActivity.class);
                startActivity(intent);
                finish();
            }
        });


        onlineBtn = findViewById(R.id.onlineBtn);
        onlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downloadUriStr.equalsIgnoreCase("")) {
                    Toast.makeText(OnlineCustomersListActivity.this, "Please Add Product", Toast.LENGTH_SHORT).show();

                } else {
                   // Intent intent = new Intent(OnlineCustomersListActivity.this, SendFinalPaymentActivity.class);
                   // startActivity(intent);
                    //finish();
                }

            }
        });
/*
        attendwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OnlineCustomersListActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_dialog_for_call_sales, viewGroup, false);

                Button joinBtn = dialogView.findViewById(R.id.joinBtn);
                LinearLayout msgToCallLayout = dialogView.findViewById(R.id.msgToCallLayout);
                msgToCallLayout.setVisibility(View.GONE);

                joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  String text = String.valueOf(java.util.UUID.randomUUID());
                        //  Log.e("GUID", "" + text);
                        // String text = "test123";
                        ///editText.getText().toString();

                        if (MessageBody.length() > 0) {
                            Log.e("MessageBody", "MessageBody:" + MessageBody);

                            //  Build options object for joining the conference. The SDK will merge the default
                            // one we set earlier and this one when joining.
                            JitsiMeetConferenceOptions options
                                    = new JitsiMeetConferenceOptions.Builder()
                                    .setRoom(MessageBody.trim())
                                    .build();
                            // Launch the new activity with the given options. The launch() method takes care
                            // of creating the required Intent and passing the options.
                            JitsiMeetActivity.launch(OnlineCustomersListActivity.this, options);
                            alertDialog.dismiss();
                        }
                    }
                });

                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();

            }
        });
*/


/*
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {

                    String FinalBillValue = "Payment of ₹ " + paymentTxt.getText().toString().trim() + " for " + qtyTxt.getText().toString().trim() +
                            " items purchased in store Andheri.";
                    //   TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                    TOPIC = "/topics/" + key2; //topic has to match what the receiver subscribed to
                    NOTIFICATION_TITLE = "Final Bill";
                    NOTIFICATION_MESSAGE = FinalBillValue;


                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);
                        notifcationBody.put("key1", "user");
                        notifcationBody.put("key2", downloadUriStr);

                        notification.put("to", TOPIC);
                        notification.put("data", notifcationBody);
                        Log.e(TAG, "notification: " + notification);

                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage());
                    }

                    sendNotifications(notification);
//                   FirebaseDatabase.getInstance().getReference().child("Tokens").
//                           child(tokenVal).child("token").
//                           addListenerForSingleValueEvent(new ValueEventListener() {
//                               @Override
//                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                   String mobileNoVal = dataSnapshot.getValue(String.class);
//                                   Log.e("token", "mobileNoVal" + FirebaseDatabase.getInstance().getReference().child("Tokens"));
//                                   Log.e("tokenn", "mobileNoVal" + FirebaseDatabase.getInstance().getReference().child("Tokens").
//                                           child(tokenVal).child("token"));
//                                   Log.e("mobileNoVal", "mobileNoVal" + mobileNoVal);
//                                   sendNotifications(mobileNoVal, NOTIFICATION_TITLE, NOTIFICATION_MESSAGE);
//                               }
//
//                               @Override
//                               public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                               }
//                           });
//                   JSONObject notification = new JSONObject();
//                   JSONObject notifcationBody = new JSONObject();
//                   try {
//                       notifcationBody.put("title", NOTIFICATION_TITLE);
//                       notifcationBody.put("message", NOTIFICATION_MESSAGE);
//                       notifcationBody.put("key1", "GoToUser");
//
//                       notification.put("to", TOPIC);
//                       notification.put("data", notifcationBody);
//                   } catch (JSONException e) {
//                       Log.e(TAG, "onCreate: " + e.getMessage() );
//                   }
//                   Log.e("notification111",""+notification);
//                   sendNotification(notification);

                }

            }
        });
*/

    }

    private boolean ValidateScreenShot() {
        if (downloadUriStr.equalsIgnoreCase("")) {
            Toast.makeText(OnlineCustomersListActivity.this, "Click on submit button.Please wait.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void takeScreenShot() {
        try {
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "Streaming" + ".jpg";
            // File storageDir = new File(Environment.getExternalStorageDirectory(), "Streaming");

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);
            Log.e("File", "===" + imageFile);
            Uri uri = Uri.fromFile(imageFile);
            uploadImage(uri);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(Uri image) {
        Log.e("URI", "----" + image);
        if (image != null) {

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + "jpg");
            mUploadTask = fileReference.putFile(image);
            Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        Log.e("URLImge", "" + downloadUri);
                        editor.remove("ImageValue").commit();
                        downloadUriStr = String.valueOf(downloadUri);
                        editor.putString("ImageValue", String.valueOf(downloadUri));
                        ImageValue = prefs.getString("ImageValue", "");

                        Log.e("ImageValue", "" + ImageValue);

                        editor.commit();
                        editor.apply();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
            //getFileExtension(mImageUri));
/*
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                  //  mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(OnlineCustomersListActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            ProductDetails upload = new ProductDetails("IMages",
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OnlineCustomersListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                           // mProgressBar.setProgress((int) progress);
                        }
                    });
*/
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }


    private boolean isValid(EditText edt_mail) {


        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (edt_mail == null)
            return false;
        return pat.matcher(edt_mail.getText().toString()).matches();

    }

    private boolean ValidateData() {

        //  firstname,lastname,email,aadharNo,mobile;
        if (firstname.getText().toString().equalsIgnoreCase("")) {
            firstname.setError("Please enter firstname");
            return false;
        }
        if (lastname.getText().toString().equalsIgnoreCase("")) {
            lastname.setError("Please enter lastname");
            return false;
        }
        if (email.getText().toString().equalsIgnoreCase("")) {
            email.setError("Please enter E-mail Id");
            return false;
        } else if (!isValid(email)) {
            email.setError("Invalid Email Address");
            return false;
        }
        if (aadharNo.getText().toString().equalsIgnoreCase("")) {
            aadharNo.setError("Please enter Aadhar Card number");
            return false;
        } else if (!validateAadharNumber(aadharNo.getText().toString())) {
            aadharNo.setError("Invalid Aadhar Card number");
            return false;
        } /*else if (aadharList.contains(aadharNo.getText().toString())) {
            aadharNo.setError("This Aadhar Card number is already exist");
            return false;
            //  Toast.makeText(getApplicationContext(), "This Aadhar number is already exist", Toast.LENGTH_LONG).show();
        } */
        if (mobile.getText().toString().equalsIgnoreCase("")) {
            mobile.setError("Please enter mobile number");
            return false;
        } else if (mobile.getText().toString().length() < 10) {
            mobile.setError("Please Enter valid  Mobile Number.");
            return false;
        } else if (!(mobile.getText().toString().startsWith("7") || mobile.getText().toString().startsWith("8") || mobile.getText().toString().startsWith("9"))) {
            mobile.setError("Please Enter valid  Mobile Number.");
            return false;
        }

        return true;
    }

    private void sendNotifications(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constant.FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        editor.remove("ImageValue").commit();
                        editor.remove("ImageVal").commit();
                        OnlineLayout.setVisibility(View.GONE);
                        OfflineModeLayout.setVisibility(View.VISIBLE);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OnlineCustomersListActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                        editor.remove("ImageValue").commit();
                        editor.remove("ImageVal").commit();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constant.serverKey);
                params.put("Content-Type", Constant.contentType);
                return params;
            }
        };
        MySingleton.getInstance(OnlineCustomersListActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        //  mImageUri = data.getData();

                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String ImageValue1 = Base64.encodeToString(b, Base64.DEFAULT);
                        editor.remove("ImageVal").commit();
                        editor.putString("ImageVal", ImageValue1);

                        editor.commit();
                        editor.apply();

                        //  PhotoImg.setImageBitmap(selectedImage);
                        mImageUri = getImageUri(getApplicationContext(), selectedImage);
                        PhotoImgVal.setVisibility(View.VISIBLE);

                        Picasso.with(this).load(mImageUri).into(PhotoImgVal);

                        Log.e("mImageUri", "" + mImageUri);

                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        String pictureFile = "" + timeStamp;
                        // File storageDir = Environment.getExternalStorageDirectory(Environment.DIRECTORY_PICTURES+"/Streaming");
                        File storageDir = new File(Environment.getExternalStorageDirectory(), "Streaming");
                        if (!storageDir.exists()) {
                            if (!storageDir.mkdirs()) {
                                Log.e("App", "failed to create directory");
                            }
                        }
                        File image = null;
                        try {
                            image = File.createTempFile(pictureFile, ".jpg", storageDir);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            FileOutputStream out = new FileOutputStream(image);
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        filePathURI = null;
                        filePathURI = Uri.fromFile(image);
                        Log.e("CameraImgPath", "" + image);
                        // uploadImage(image);


                        editor.commit();
                        editor.apply();
                        //   String pictureFilePath = image.getAbsolutePath();
                        //  Log.e("CameraImg Path", "" + pictureFilePath);
                        //  Uri ImgUrl = data.getData();
//                        FirebaseStorage storage = FirebaseStorage.getInstance();
//                        String myFolder = "images";
//                        //  StorageReference storageRef = storage.child(myFolder).child("images/pic.jpg");
//                        // Log.e("CameraImg ImgUrl", "" + ImgUrl);
//
//                        StorageReference storageReference;
//                        Uri filePath=null;
//                        filePath=Uri.fromFile(image);
//                        Log.e("FilePath",""+filePath);
//
//
//
//                         storage = FirebaseStorage.getInstance();
//                         storageReference = storage.getReferenceFromUrl("gs://fashionapp-2761b.appspot.com/ProductImages").child("sample.jpg");
//
//
//                       // UploadTask uploadTask = storageReference.putFile(Uri.fromFile(file));
//
//                        UploadTask uploadTask = storageReference.putBytes(b);
//                        uploadTask.addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            }
//                        });
//


                        // }
                    }
                    break;


            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(OnlineCustomersListActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;
            case R.id.billLay:
                Intent intent1 = new Intent(OnlineCustomersListActivity.this, ListOfPaymentActivity.class);
                startActivity(intent1);

                break;
            case R.id.logout:

                editor.putString("IsLoggedInSales", "0");

                editor.commit();
                editor.apply();
                SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);

                agentId = prefs.getString("ID", "");

                Log.e("AgentId", "AgentId" + agentId);
                BankAgents agent = new BankAgents(agentId, StoreName, AgentName, MobileNo, AgentPhoto, "false", AgentToken);
                // tokenn.setId(user.getUid());
                //  tokenn.setToken(token);
                // databaseref.child(id).setValue(tokenn);
                // mDatabase.child("BankAgents").setValue(agent);
                mDatabase.child(agentId).setValue(agent);

                Intent intent122 = new Intent(OnlineCustomersListActivity.this, SplashScreenActivity.class);
                startActivity(intent122);
                finish();


                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    private boolean validateData() {
        if (paymentTxt.getText().toString().equalsIgnoreCase("")) {
            paymentTxt.setError("Please enter the amount");
            return false;
        } else if (qtyTxt.getText().toString().equalsIgnoreCase("")) {
            qtyTxt.setError("Please enter the quantity");
            return false;
        } else if (downloadUriStr.equalsIgnoreCase("")) {
            Toast.makeText(OnlineCustomersListActivity.this, "Please Add Product", Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OnlineCustomersListActivity.this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ALL_PERMISSION:

        }
    }

    public class CustListAdapter extends RecyclerView.Adapter<CustListAdapter.ViewHolder> {
        ArrayList<String> custList, mobileNoList;
        Context context;
        private AlertDialog alertDialog;
        private URL serverURL;


        public CustListAdapter(Context context, ArrayList<String> custList) {

            this.context = context;
            this.custList = custList;
        }

        public CustListAdapter(Context context, ArrayList<String> custList, ArrayList<String> mobileNoList) {
            this.context = context;
            this.custList = custList;
            this.mobileNoList = mobileNoList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.cust_list_item, parent, false);
            CustListAdapter.ViewHolder viewHolder = new CustListAdapter.ViewHolder(listItem);

            try {
                serverURL = new URL("https://meet.jit.si");//test
                //serverURL = new URL("https://meet.ezycom.co.in/");
                //https://meet.ezycom.co.in/
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Invalid server URL!");
            }
            JitsiMeetConferenceOptions defaultOptions
                    = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setWelcomePageEnabled(false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustListAdapter.ViewHolder holder, int position) {
            //  final MyListData myListData = listdata[position];
            holder.CustomerTokenTxt.setText("Meeting Id :" + custList.get(position));
            holder.CustomerMobileTxt.setText("MobileNo :" + mobileNoList.get(position));

            holder.CustomerDeatilsLay.setTag(R.string.tag1, mobileNoList.get(position));
            holder.CustomerDeatilsLay.setTag(R.string.tag2, custList.get(position));

            holder.CustomerDeatilsLay.setOnClickListener(new
                                                                 View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View view) {
                                                                         //   String SelectedMobile = String.valueOf(view.getTag(R.string.tag1));
                                                                         String SelectedMobile = view.getTag(R.string.tag1).toString();
                                                                         String SelectedToken = view.getTag(R.string.tag2).toString();
                                                                         editor.remove("key2").clear().commit();
                                                                         editor.putString("key2", SelectedMobile);
                                                                         editor.commit();
                                                                         editor.apply();
                                                                         Log.e("SelectedMobile", "==" + SelectedMobile);
                                                                         //    if (holder.CustomerTokenTxt.getText().toString().equalsIgnoreCase("Meeting Id :" + String.valueOf(custList.get(0)))) {
                                                                         // Log.e("CustomerMobileTxt", "" + String.valueOf(custList.get(custList.size() - 1)));


                                                                         AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                                         ViewGroup viewGroup = view.findViewById(android.R.id.content);
                                                                         View dialogView = LayoutInflater.from(context).inflate(R.layout.agent_dialog_for_call_sales, viewGroup, false);
                                                                         //   String text = String.valueOf(java.util.UUID.randomUUID());
                                                                         //   Log.e("GUID", "" + text);
                                                                         Log.e("tokenVal===", "" + custList.get(0));
                                                                         // SharedPreferences prefs = mContext.getSharedPreferences("Fashion", MODE_PRIVATE);
                                                                         //   tokenVal = prefs.getString("tokenVal", "");
                                                                         Button joinBtn = dialogView.findViewById(R.id.attendwBtn);
                                                                         EditText editText = dialogView.findViewById(R.id.conferenceName);
                                                                         joinBtn.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View view) {


                                                                                 runOnUiThread(new Runnable() {
                                                                                     public void run() {
                                                                                         // UI code goes here


////////----------------To start Meeting------------------

                                                                                         if (custList.size() > 0) {
                                                                                             // Build options object for joining the conference. The SDK will merge the default
                                                                                             // one we set earlier and this one when joining.
                                                                                             JitsiMeetConferenceOptions options
                                                                                                     = new JitsiMeetConferenceOptions.Builder()
                                                                                                     .setRoom(SelectedToken)
                                                                                                     .build();
                                                                                             // Launch the new activity with the given options. The launch() method takes care
                                                                                             // of creating the required Intent and passing the options.
                                                                                             JitsiMeetActivity.launch((OnlineCustomersListActivity) context, options);
                                                                                             alertDialog.dismiss();
                                                                                             OnlineLayout.setVisibility(View.VISIBLE);
                                                                                             OfflineModeLayout.setVisibility(View.GONE);


                                                                                         }

                                                                                     }
                                                                                 });
//                            FirebaseDatabase.getInstance().getReference().child("Agents").
//                                    child("agentToken").child(tokenVal).
//                                    addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            String usertoken = dataSnapshot.getValue(String.class);
//                                            Log.e("token", "usertoken" + FirebaseDatabase.getInstance().getReference().child("Agents").child("agentToken").child(tokenVal));
//                                            Log.e("tokenn", "usertoken1" + FirebaseDatabase.getInstance().getReference().child("Tokens").child(tokenVal));
//                                            Log.e("usertoken", "usertoken2" + usertoken);
//                                            Log.e("dataSnapshot", "usertoken3" + dataSnapshot.getValue());
//////----------------To start Meeting------------------
//
//                                            if (text.length() > 0) {
//                                                // Build options object for joining the conference. The SDK will merge the default
//                                                // one we set earlier and this one when joining.
//                                                JitsiMeetConferenceOptions options
//                                                        = new JitsiMeetConferenceOptions.Builder()
//                                                        .setRoom(text)
//                                                        .build();
//                                                // Launch the new activity with the given options. The launch() method takes care
//                                                // of creating the required Intent and passing the options.
//                                                JitsiMeetActivity.launch((ListOfAgentsActivity) mContext, options);
//                                                alertDialog.dismiss();
//
//
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
                                                                                 // }
                                                                             }

                                                                         });

                                                                         builder.setView(dialogView);
                                                                         alertDialog = builder.create();
                                                                         alertDialog.show();


                                                                     }
                                                                 });


        }


        @Override
        public int getItemCount() {
            return custList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView CustomerTokenTxt, CustomerMobileTxt;
            public LinearLayout CustomerDeatilsLay;

            public ViewHolder(View itemView) {
                super(itemView);
                this.CustomerTokenTxt = (TextView) itemView.findViewById(R.id.CustomerTokenTxt);
                this.CustomerMobileTxt = (TextView) itemView.findViewById(R.id.CustomerMobileTxt);
                this.CustomerDeatilsLay = (LinearLayout) itemView.findViewById(R.id.CustomerDeatilsLay);

            }
        }
    }

}

