/*
package com.shawinfosolutions.bankagentapp;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

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
        ViewHolder viewHolder = new ViewHolder(listItem);

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        //  final MyListData myListData = listdata[position];
        holder.CustomerTokenTxt.setText("Meeting Id :" + custList.get(position));
        holder.CustomerMobileTxt.setText("MobileNo :" + mobileNoList.get(position));
        holder.CustomerDeatilsLay.setOnClickListener(new
                                                             View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View view) {

                                                                     if (holder.CustomerTokenTxt.getText().toString().equalsIgnoreCase("Meeting Id :"+ String.valueOf(custList.get(0)))) {
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
                                                                                                     .setRoom(custList.get(0))
                                                                                                     .build();
                                                                                             // Launch the new activity with the given options. The launch() method takes care
                                                                                             // of creating the required Intent and passing the options.
                                                                                             JitsiMeetActivity.launch((OnlineCustomersListActivity) context, options);
                                                                                             alertDialog.dismiss();


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
                                                                 }
                                                             });


    }


    @Override
    public int getItemCount() {
        return custList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
*/
