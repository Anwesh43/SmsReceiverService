package com.example.anweshmishra.smsreceiverservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.provider.Telephony.*;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.anweshmishra.smsreceiverservice.models.RequestFallback;
import com.example.anweshmishra.smsreceiverservice.runnables.RequestRunner;

/**
 * Created by anweshmishra on 21/06/15.
 */
public class ReceiverService extends IntentService{
    SmsReceiver smsReceiver;
    SharedPreferences sharedPreferences;
    RequestFallback requestFallback;
    Thread requestSenderThread;
    RequestRunner requestRunner;
    IntentFilter smsIntentFilter;
    public ReceiverService() {
        super("SmsReceiverService");
    }
    public void onCreate() {

        sharedPreferences = getSharedPreferences("MOBILE_NUMBER_SAVER",MODE_PRIVATE);
        smsIntentFilter = new IntentFilter();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestFallback = new RequestFallback(requestQueue);
        smsReceiver = new SmsReceiver(requestQueue,sharedPreferences,requestFallback);
        smsIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        requestRunner = new RequestRunner(requestFallback);
        requestSenderThread = new Thread(requestRunner);
        registerReceiver(smsReceiver,smsIntentFilter);
    }
    public int onStartCommand(Intent intent,int startId,int flags) {

        requestRunner.init();
        requestFallback.init();
        requestSenderThread.start();

        return START_STICKY;
    }
    public void onHandleIntent(Intent intent) {

    }
    public void onDestroy() {
        unregisterReceiver(smsReceiver);
        requestRunner.stop();
            while(true) {
                try {
                    requestSenderThread.join();
                    break;
                }
                catch (Exception exception) {

                }
            }
        }
    }

