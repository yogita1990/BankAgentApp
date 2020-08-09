package com.shawinfosolutions.bankagentapp;

import java.security.SecureRandom;
import java.util.Random;

public class Constant {
    public static String UserTopicName="userABC";
    // public static String AgentTopicName="userAGENT";
    static final private String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    static final private Random rng = new SecureRandom();
    public static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    public static String contentType = "application/json";
    public static String serverKey =
            "key=" +"AAAAStyfArA:APA91bFBLG-CmpHJKJEOu-OVtRF9jh8PE2pK3vHR2c295hXVj9YkGp8cCJkZT3sqbDGd_IqyESYNnfxJBxwIB5nEQmky2-43lpfq5EJPMde7TXYYYlb3JVn9hx4TTtg-4PS1M8nC6fU_";




    static char randomChar(){
        return ALPHABET.charAt(rng.nextInt(ALPHABET.length()));
    }

    public static String randomUUID(int length, int spacing, char spacerChar){
        StringBuilder sb = new StringBuilder();
        int spacer = 0;
        while(length > 0){
            if(spacer == spacing){
                sb.append(spacerChar);
                spacer = 0;
            }
            length--;
            spacer++;
            sb.append(randomChar());
        }
        return sb.toString();
    }
}
