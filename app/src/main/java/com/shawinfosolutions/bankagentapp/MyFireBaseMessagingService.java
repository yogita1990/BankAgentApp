package com.shawinfosolutions.bankagentapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shawinfosolutions.bankagentapp.Model.MeetingData;
import com.shawinfosolutions.bankagentapp.Model.PaymentDetails;


import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    //private final String ADMIN_CHANNEL_ID = String.valueOf(R.string.default_notification_channel_id);
    //"@string/default_notification_channel_id";
    private final String ADMIN_CHANNEL_ID = "admin_channel";
    //  private final String AGENT_CHANNEL_ID = "AGENT_channel";
    private String key1 = "";
    private Intent intent;
    private String key2="";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.e("remoteMessage", "" + remoteMessage.getData().get("title"));
            Log.e("remoteMessageData", "" + remoteMessage.getData().get("message"));
            key1 = remoteMessage.getData().get("key1");
            key2 = remoteMessage.getData().get("key2");
            Log.e("key1", "" + remoteMessage.getData().get("key1"));
            Log.e("key2", "" + remoteMessage.getData().get("key2"));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (key1.toString().equalsIgnoreCase("user")) {
//            intent = new Intent(this, PaymentActivity.class);
//
//
//        } else
        if (key1.toString().equalsIgnoreCase("agentList")) {
            intent = new Intent(this, OnlineCustomersListActivity.class);
            intent.putExtra("key2",key2);

            intent.putExtra("MessageTitle", "" + remoteMessage.getData().get("title"));
            intent.putExtra("MessageBody", "" + remoteMessage.getData().get("message"));
            Date currentTime = Calendar.getInstance().getTime();

            String  DateTime=String.valueOf(currentTime);

            MeetingData meetingData = new MeetingData(remoteMessage.getData().get("message"),remoteMessage.getData().get("key2"),DateTime);

            // mDatabase.child("Agents").setValue(agent);
            //  mDatabase.setValueAsync(users);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("MeetingData");
            String   idKey  = mDatabase.push().getKey();

            mDatabase.child(idKey).setValue(meetingData);

        }else if(key1.toString().equalsIgnoreCase("agentListPayment")){
            intent.putExtra("key2",key2);

            intent.putExtra("MessageTitle", "" + remoteMessage.getData().get("title"));
            intent.putExtra("MessageBody", "" + remoteMessage.getData().get("message"));

            intent = new Intent(this, ListOfPaymentActivity.class);

            PaymentDetails paymentDetails = new PaymentDetails(remoteMessage.getData().get("message"),remoteMessage.getData().get("key2"));

            // mDatabase.child("Agents").setValue(agent);
            //  mDatabase.setValueAsync(users);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PaymentDetails");
            String   idKey  = mDatabase.push().getKey();

            mDatabase.child(idKey).setValue(paymentDetails);
        }

        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        // Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        // to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.sis);

        Uri notificationSoundUri;

            notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.sis)
                .setLargeIcon(largeIcon)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setOngoing(true)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel,agentChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);

        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }


    }



//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        final Intent intent = new Intent(this, SplashScreenActivity.class);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        int notificationID = new Random().nextInt(3000);
//
//      /*
//        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
//        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
//      */
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            setupChannels(notificationManager);
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
//                R.drawable.sis);
//
//        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
//                .setSmallIcon(R.drawable.sis)
//                .setLargeIcon(largeIcon)
//                .setContentTitle(remoteMessage.getData().get("title"))
//                .setContentText(remoteMessage.getData().get("message"))
//                .setAutoCancel(true)
//                .setSound(notificationSoundUri)
//                .setContentIntent(pendingIntent);
//
//        //Set notification color to match your app color template
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
//        }
//        notificationManager.notify(notificationID, notificationBuilder.build());
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setupChannels(NotificationManager notificationManager){
//        CharSequence adminChannelName = "New notification";
//        String adminChannelDescription = "Device to devie notification";
//
//        NotificationChannel adminChannel;
//        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
//        adminChannel.setDescription(adminChannelDescription);
//        adminChannel.enableLights(true);
//        adminChannel.setLightColor(Color.RED);
//        adminChannel.enableVibration(true);
//        if (notificationManager != null) {
//            notificationManager.createNotificationChannel(adminChannel);
//        }
//    }
}
