package com.example.anweshmishra.smsreceiverservice;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.anweshmishra.smsreceiverservice.models.RequestFallback;
import com.example.anweshmishra.smsreceiverservice.utils.SmsMessageUtil;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by anweshmishra on 21/06/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    RequestFallback requestFallback;
    public SmsReceiver(RequestQueue requestQueue,SharedPreferences sharedPreferences,RequestFallback requestFallback) {
        this.requestQueue = requestQueue;
        this.sharedPreferences = sharedPreferences;
        this.requestFallback = requestFallback;
    }
    public void onReceive(final Context context,Intent intent) {
        SmsMessage[] smsMessages = SmsMessageUtil.createMessageFromIntent(intent);
        String smsReceived = SmsMessageUtil.getMessageBody(smsMessages);
        if (smsMessages.length != 0) {
            String phoneNumber = SmsMessageUtil.getPhoneNumberFromMessage(smsMessages[0]);
            StringRequest request = RequestFactory.makeRequestFromMessage(context,this,smsReceived,phoneNumber);

            try {
                if(request!=null) {
                    Toast.makeText(context, "request is added in queue", Toast.LENGTH_LONG).show();
                    requestFallback.addRequest(request);
                }
            }
            catch (Exception exception) {

            }
        }
    }
    public void addCount(String mobileNumber) {
        int mc_count = sharedPreferences.getInt(mobileNumber + "_count", 0);
        Long mc_interval = sharedPreferences.getLong(mobileNumber + "_last_request", System.currentTimeMillis());
        if(System.currentTimeMillis()-mc_interval >= getDayInMilliSeconds()) {
            mc_count = 0;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(mobileNumber+"_count",mc_count+1);
        editor.putLong(mobileNumber+"_last_request",System.currentTimeMillis());
        editor.commit();
    }
    public boolean isMobileNumberAttacking(String mobileNumber) {
        int count = sharedPreferences.getInt(mobileNumber+"_count", 0);
        return count > 4;
    }

    private Long getDayInMilliSeconds() {
        return 4*60*60*1000L;
    }
}
