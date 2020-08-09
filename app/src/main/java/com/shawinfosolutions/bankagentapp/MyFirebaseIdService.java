package com.shawinfosolutions.bankagentapp;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.shawinfosolutions.bankagentapp.Model.Token;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("refreshToken", "1111" + s);
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("refreshToken111", "2211" + refreshToken);

        if (firebaseUser != null) {
            updateToken(refreshToken);
        }

    }


    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Token token1 = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("BankAgents").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }
}
