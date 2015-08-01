package com.example.anweshmishra.smsreceiverservice.utils;

import android.content.Intent;
import android.telephony.SmsMessage;

import com.example.anweshmishra.smsreceiverservice.RequestType;

/**
 * Created by anweshmishra on 01/08/15.
 */
public class SmsMessageUtil {
    public static SmsMessage[] createMessageFromIntent(Intent intent) {
        StringBuilder messageBuilder = new StringBuilder("");
        Object[] pdus = (Object [])intent.getExtras().get("pdus");
        SmsMessage[] smsMessages= new SmsMessage[pdus.length];
        for(int i=0;i<pdus.length;i++) {
            byte[] messageBytes = (byte [])pdus[i];
            smsMessages[i] = SmsMessage.createFromPdu(messageBytes);

        }
        return smsMessages;
    }
    public static String getPhoneNumberFromMessage(SmsMessage smsMessage) {
        return smsMessage.getDisplayOriginatingAddress();
    }
    public static String getMessageBody(SmsMessage[] smsMessages) {
        StringBuilder messages = new StringBuilder("");
        for(int i=0;i<smsMessages.length;i++) {
            messages.append(smsMessages[i].getMessageBody());
        }
        return messages.toString();
    }
    public static RequestType getRequestType(String message) {
        String[] messages = message.split(" ");
        RequestType requestType = RequestType.APPOINTMENT;
        if(messages.length != 0) {
            if(messages[messages.length-1].equals("CL")) {
                requestType = RequestType.CANCELLATION;
            }
        }
        return requestType;
    }
}
