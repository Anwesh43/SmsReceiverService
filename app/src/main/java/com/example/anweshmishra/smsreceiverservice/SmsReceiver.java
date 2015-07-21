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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by anweshmishra on 21/06/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    public SmsReceiver(RequestQueue requestQueue,SharedPreferences sharedPreferences) {
        this.requestQueue = requestQueue;
        this.sharedPreferences = sharedPreferences;
    }
    public void onReceive(final Context context,Intent intent) {

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        SmsMessage smsMessages[] = new SmsMessage[pdus.length];
        StringBuilder smsReceived = new StringBuilder("");
        //StringBuilder smsReceivedFrom = new StringBuilder("");
        for (int i = 0; i < pdus.length; i++) {
            smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            smsReceived.append(smsMessages[i].getMessageBody());
        }
        if (smsMessages.length != 0) {
            final Date recievedOn = new Date(smsMessages[0].getTimestampMillis());
            final String message = smsReceived.toString();
            final String[] messageBody = message.split(" ");
            String spSpec = "";
            StringBuilder customerNameSpec = new StringBuilder("");
            if(messageBody.length >= 1) {
                spSpec = messageBody[0];
                for(int i=1;i<messageBody.length;i++) {
                    customerNameSpec.append(messageBody[i]);
                }
            }
            final String spNo = spSpec;
            final String customerName = customerNameSpec.toString();
            smsReceived.append(" from ");
            final String phoneNumber = smsMessages[0].getDisplayOriginatingAddress();
            smsReceived.append(smsMessages[0].getDisplayOriginatingAddress());
            Toast.makeText(context, smsReceived.toString(), Toast.LENGTH_SHORT).show();
            if(ValidatorUtil.validNumber(spNo) && ValidatorUtil.validMobileNumber(phoneNumber.substring(3))) {
                addCount(phoneNumber);
                if(!isMobileNumberAttacking(phoneNumber)) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("spNo", spNo);
                    params.put("senderNo", phoneNumber);
                    params.put("customerName", customerName);
                    StringRequest stringRequest = RequestUtil.makeRequest(context, params);
                    requestQueue.add(stringRequest);
                }
                else {
                 Toast.makeText(context,"this "+phoneNumber+" is an asshole who is trying to fuck my system has sent "+sharedPreferences.getInt(phoneNumber+"_count",0)+"Requests",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(context,"Validation failed",Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void addCount(String mobileNumber) {
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
    private boolean isMobileNumberAttacking(String mobileNumber) {
        int count = sharedPreferences.getInt(mobileNumber+"_count", 0);
        return count > 4;
    }

    private Long getDayInMilliSeconds() {
        return 4*60*60*1000L;
    }
}
