package com.shawinfosolutions.bankagentapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.shawinfosolutions.bankagentapp.Model.BankAgents;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    private TextView addPhoto;
    private ImageView PhotoImg;
    private AlertDialog alertDialog;
    private String TAG = "RegisterActivity.class";
    private ActionBar actionbar;
    private Button submitBtn;
    private static final int REQUEST_ALL_PERMISSION = 200;
    private EditText edt_agentName, edt_mobileNo;
    private String[] permissions = {


            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};
    private DatabaseReference mDatabase;
    private String StoreLocation = "";
    private String ImageValue;
    private String token="testABC";
    private String idKey="";
    private String ID="", StoreName="",AgentName="",AgentPhoto="",AgentLoginStatus="",AgentToken="",MobileNo="";
    private ArrayList<String> mobileList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_as_agent);

        actionbar = getSupportActionBar();
        actionbar.setTitle("Executive Registration");
        mobileList=new ArrayList<>();
        mobileList.clear();
        //actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        addPhoto = findViewById(R.id.addPhoto);
        submitBtn = findViewById(R.id.submitBtn);
        PhotoImg = findViewById(R.id.PhotoImg);
        edt_agentName = findViewById(R.id.edt_agentName);
        edt_mobileNo = findViewById(R.id.edt_mobileNo);


        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("BankAgents");
        SharedPreferences sharedpreferences = getSharedPreferences("Fashion", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedpreferences.edit();
        //  mDatabase = FirebaseDatabase.getInstance().getReference();
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Your Branch");
        categories.add("CST");
        categories.add("Kurla");
        categories.add("Mulund");
        categories.add("Bhandup");
        categories.add("Thane");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ALL_PERMISSION);
        } else {

        }
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            BankAgents agent = new BankAgents((String) userData.get("id"),(String) userData.get("storeName"),(String) userData.get("agentName")
                                    ,(String) userData.get("agentmobileNo"),(String) userData.get("agentPhoto"),
                                    (String) userData.get("agentLoginStatus"),(String) userData.get("agentToken"));
                            if (
                                    (agent.getId() != null && !agent.getId().isEmpty() && !agent.getId().equals("null"))&&
                                            (agent.getBranchName() != null && !agent.getBranchName().isEmpty() && !agent.getBranchName().equals("null"))&&
                                            (agent.getAgentName() != null && !agent.getAgentName().isEmpty() && !agent.getAgentName().equals("null")) &&
                                            (agent.getAgentmobileNo() != null && !agent.getAgentmobileNo().isEmpty() && !agent.getAgentmobileNo().equals("null"))&&
                                            (agent.getAgentPhoto() != null && !agent.getAgentPhoto().isEmpty() && !agent.getAgentPhoto().equals("null"))&&
                                            (agent.getAgentLoginStatus() != null && !agent.getAgentLoginStatus().isEmpty() && !agent.getAgentLoginStatus().equals("null"))&&
                                            (agent.getAgentToken() != null && !agent.getAgentToken().isEmpty() && !agent.getAgentToken().equals("null"))
                            ){

                                ID = agent.getId();
                                StoreName = agent.getBranchName();
                                AgentName = agent.getAgentName();
                                MobileNo = agent.getAgentmobileNo();
                                AgentPhoto = agent.getAgentPhoto();
                                AgentLoginStatus = agent.getAgentLoginStatus();
                                AgentToken = token;
                                mobileList.add(MobileNo);
                                Log.e("mobile","mob=="+MobileNo);

                            }
//addTextToView(mUser.getName() + " - " + Integer.toString(mUser.getAge()));

                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
// If the object can’t be casted into HashMap, it means that it is of type String. 

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        PhotoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.take_a_photo, viewGroup, false);
                Button fromCameraBtn = dialogView.findViewById(R.id.fromCameraBtn);
                Button fromGalleryBtn = dialogView.findViewById(R.id.fromGalleryBtn);

                fromCameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                        alertDialog.dismiss();
                    }
                });

                fromGalleryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                        alertDialog.dismiss();

                    }
                });

                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();

            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.take_a_photo, viewGroup, false);
                Button fromCameraBtn = dialogView.findViewById(R.id.fromCameraBtn);
                Button fromGalleryBtn = dialogView.findViewById(R.id.fromGalleryBtn);

                fromCameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                        alertDialog.dismiss();
                    }
                });

                fromGalleryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                        alertDialog.dismiss();

                    }
                });

                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mobileList.contains(edt_mobileNo.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "This mobile number is already used.", Toast.LENGTH_SHORT).show();

                }
                    Log.e("Loc","Loc="+StoreLocation);
                    Log.e("name","name="+edt_agentName.getText().toString());
                    Log.e("mob","mob=="+edt_mobileNo.getText().toString());
                    Log.e("ImageValue","ImageValue="+ImageValue);
                    Log.e("Status","Status="+"false");

                    editor.putString("StoreName", StoreName);
                    editor.putString("AgentName", AgentName);
                    editor.putString("MobileNo", edt_mobileNo.getText().toString());
                    editor.putString("AgentPhoto", AgentPhoto);

                    editor.putString("AgentLoginStatus", AgentLoginStatus);
                    editor.commit();
                    editor.apply();

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token
                                    token = task.getResult().getToken();
                                    editor.putString("AgentToken", token);
                                    editor.commit();
                                    editor.apply();
                                    Log.e("token===","token="+token);
                                    // Log and toast
                                    //  String msg = getString(R.string.msg_token_fmt, token);
                                    //  Log.d(TAG, msg);
                                    // Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();

                                    //  mDatabase = FirebaseDatabase.getInstance().getReference("Customers");
                                    idKey  = mDatabase.push().getKey();

                                    BankAgents agent = new BankAgents(idKey,StoreLocation, edt_agentName.getText().toString(),
                                            edt_mobileNo.getText().toString(), ImageValue,
                                            "false", token);

                                    // mDatabase.child("Agents").setValue(agent);
                                    //  mDatabase.setValueAsync(users);

                                    mDatabase.child(idKey).setValue(agent);
                                    Log.e("id","token="+idKey);
                                    editor.putString("agentId", idKey);
                                    editor.putString("agentToken", token);
                                    editor.commit();
                                    editor.apply();
                                }
                            });





                    // editor.putString("agentId", idKey);
                    editor.putString("Mobile", edt_mobileNo.getText().toString());
                    editor.commit();
                    editor.apply();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("intentVal","1");
                    startActivity(intent);
                    finish();


            }
        });
    }


    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        ImageValue = Base64.encodeToString(b, Base64.DEFAULT);
                        PhotoImg.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                PhotoImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] b = baos.toByteArray();
                                ImageValue = Base64.encodeToString(b, Base64.DEFAULT);
                                Log.e("ImageValue",""+ImageValue);
                                cursor.close();
                            }
                        }

                    }
                    break;
                case 2:
                    Log.d(TAG, "External storage2");
                    if (resultCode == RESULT_OK && data != null) {
                        // Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                        //resume tasks needing this permission
                        // downloadPdfFile();
                    } else {
                        // progress.dismiss();
                    }
                    break;

                case 3:
                    Log.d(TAG, "External storage1");
                    if (resultCode == RESULT_OK && data != null) {
                        //  Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                        //resume tasks needing this permission
                        // SharePdfFile();
                    } else {
                        // progress.dismiss();
                    }
                    break;

            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        StoreLocation = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + StoreLocation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(RegisterActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;
            case R.id.loginItem:

                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                intent1.putExtra("intentVal","0");

                startActivity(intent1);
                finish();

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, SplashScreenActivity.class);
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

}

