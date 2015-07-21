package com.example.anweshmishra.smsreceiverservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.provider.Telephony.*;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by anweshmishra on 21/06/15.
 */
public class ReceiverService extends IntentService{
    SmsReceiver smsReceiver;
    SharedPreferences sharedPreferences;
    public ReceiverService() {
        super("SmsReceiverService");
    }
    public void onCreate() {
        sharedPreferences = getSharedPreferences("MOBILE_NUMBER_SAVER",MODE_PRIVATE);
        IntentFilter smsIntentFilter = new IntentFilter();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        smsReceiver = new SmsReceiver(requestQueue,sharedPreferences);
        smsIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver,smsIntentFilter);


    }
    public int onStartCommand(Intent intent,int startId,int flags) {
        return START_STICKY;
    }
    public void onHandleIntent(Intent intent) {

    }
    public void onDestroy() {
        unregisterReceiver(smsReceiver);
    }
}
