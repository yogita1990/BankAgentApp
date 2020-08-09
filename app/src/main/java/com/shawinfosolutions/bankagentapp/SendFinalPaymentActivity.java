/*
package com.shawinfosolutions.bankagentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SendFinalPaymentActivity extends AppCompatActivity {
    private EditText paymentTxt, qtyTxt;
    private Button payNowBtn;
    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private String TOPIC;

    //"AAAAbGFrzQ8:APA91bHQSfNwhC9XBowd4tlzzJnP7dFvDuRnq9mSQgzznbP1JRiNjzQX872NeucP1Vi8T55q57KzZOZ6Q0ReMCjNVDg88xe2OBUNLM5nHDkf_Z2if7y-nCX0esBbAZE-SuivKB5HWh3d";
    final String TAG = "NOTIFICATION TAG";
    private String tokenVal;
    private String mobileNoVal = "";
    private String ImageVal;
    // private APIService apiService;
    private ImageView PhotoImg;
    private String myBase64Image;
    private SharedPreferences.Editor editor;
    private String ImageValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_final_pay_layout);
        paymentTxt = findViewById(R.id.paymentTxt);
        qtyTxt = findViewById(R.id.qtyTxt);
        payNowBtn = findViewById(R.id.payNowBtn);
        PhotoImg=findViewById(R.id.PhotoImg);
        SharedPreferences prefs = getSharedPreferences("Fashion", MODE_PRIVATE);
        tokenVal = prefs.getString("tokenVal", "");
        editor = prefs.edit();
        String key2 = prefs.getString("key2", "");
        //  mobileNoVal = prefs.getString("key2", "");//"No name defined" is the de
        Log.e("key2", "" + mobileNoVal);
        prefs = getSharedPreferences("Fashion", MODE_PRIVATE);
        ImageVal = prefs.getString("ImageVal", "");
        ImageValue = prefs.getString("ImageValue", "");
        if(ImageValue!=null && ImageVal !=null){
            Log.e("ImageValue111", "" + ImageValue);
            Log.e("ImageVal", "" + ImageVal);

            // byte[] byteArr = ImageVal.getBytes();
            byte [] encodeByte= Base64.decode(ImageVal,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            PhotoImg.setImageBitmap(bitmap);
            myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            Log.e("myBase64Image", "" + myBase64Image);

            //  paymentTxt.setText("Payment of "+"\u20B9"+" 20000 "+" for "+" 2 "+"items purchased in store "+"Andheri.");

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
                            notifcationBody.put("key2", ImageValue);

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
                        Intent intent = new Intent(SendFinalPaymentActivity.this, OnlineCustomersListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }


            });

        }


    }

    private void sendNotifications(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constant.FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        editor.remove("ImageValue").commit();
                        editor.remove("ImageVal").commit();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendFinalPaymentActivity.this, "Request error", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(SendFinalPaymentActivity.this).addToRequestQueue(jsonObjectRequest);
    }


//    private void sendNotification(JSONObject notification) {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constant.FCM_API, notification,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "onResponse: " + response.toString());
//                        //edtTitle.setText("");
//                        //edtMessage.setText("");
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SendFinalPaymentActivity.this, "Request error", Toast.LENGTH_LONG).show();
//                        Log.i(TAG, "onErrorResponse: Didn't work");
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", Constant.serverKey);
//                params.put("Content-Type", Constant.contentType);
//                return params;
//            }
//        };
//        MySingleton.getInstance(SendFinalPaymentActivity.this).addToRequestQueue(jsonObjectRequest);
//    }

    private boolean validateData() {
        if (paymentTxt.getText().toString().equalsIgnoreCase("")) {
            paymentTxt.setError("Please enter the amount");
            return false;
        } else if (qtyTxt.getText().toString().equalsIgnoreCase("")) {
            qtyTxt.setError("Please enter the quantity");
            return false;
        }
        return true;
    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(SendFinalPaymentActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                //  finish();
                break;


        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SendFinalPaymentActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

*/
