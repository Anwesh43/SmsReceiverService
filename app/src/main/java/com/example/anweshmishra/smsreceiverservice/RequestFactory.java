package com.example.anweshmishra.smsreceiverservice;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.example.anweshmishra.smsreceiverservice.utils.SmsMessageUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anweshmishra on 30/07/15.
 */
public class RequestFactory {
       public static StringRequest makeRequestFromMessage(Context context,SmsReceiver receiver,String message,String phoneNumber) {
           if(SmsMessageUtil.getRequestType(message) == RequestType.APPOINTMENT) {
               return getAppointmentRequest(context,receiver,message,phoneNumber);
           }
           else {
               return getCancellationRequest(context,receiver,message,phoneNumber);
           }
       }
       private static StringRequest getAppointmentRequest(Context context,SmsReceiver receiver,String message,String phoneNumber) {
           String[] messages = message.split(" ");
           if(messages.length > 0) {
               String spNo = messages[0],customerName = "";
               String[] keys = {"spNo","customerName","senderNo"};
               if (messages.length == 2) {
                    customerName = messages[1];
               }
               String[] values = {spNo,customerName,phoneNumber};
               if(ValidatorUtil.validNumber(spNo) && ValidatorUtil.validMobileNumber(phoneNumber.substring(3))) {
                   return makeSafeRequest(context,receiver,getParamsMapToSend(keys,values),phoneNumber,AppConfig.APPOINTMENT_SERVICE);
               }
               else {
                   Toast.makeText(context,"Validation failed",Toast.LENGTH_SHORT).show();
               }
           }
           return null;
       }
      private static HashMap<String,String> getParamsMapToSend(String[] keys,String[] values) {
          HashMap<String,String> paramsMap = new HashMap<String,String>();
          int index = 0;
          for(String key:keys) {
              paramsMap.put(key,values[index]);
              index++;
          }
          return paramsMap;
      }
    private static StringRequest getCancellationRequest(Context context,SmsReceiver receiver,String message,String phoneNumber) {
        String[] messages = message.split(" ");
        if(messages.length > 0) {
            String bookingId = messages[0];
            String[] keys = {"incomingVO.senderPhNumber","incomingVO.bookingID"},values = {phoneNumber,bookingId};
            return makeSafeRequest(context,receiver,getParamsMapToSend(keys,values),phoneNumber,AppConfig.CANCELLATION_SERVICE);
        }
        return null;
    }
    private static StringRequest makeSafeRequest(Context context,SmsReceiver receiver,HashMap<String,String> params,String phoneNumber,String service) {
        receiver.addCount(phoneNumber);
        if(!receiver.isMobileNumberAttacking(phoneNumber)) {
            return RequestUtil.makeRequest(context,params,service);
        }
        else {
            Toast.makeText(context, "this " + phoneNumber + " is an asshole who is trying to fuck my system has sent " + receiver.sharedPreferences.getInt(phoneNumber + "_count", 0) + "Requests", Toast.LENGTH_LONG).show();
        }
        return null;
    }

}
