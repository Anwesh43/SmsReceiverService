package com.example.anweshmishra.smsreceiverservice.runnables;

import com.example.anweshmishra.smsreceiverservice.AppConfig;
import com.example.anweshmishra.smsreceiverservice.RequestUtil;
import com.example.anweshmishra.smsreceiverservice.models.RequestFallback;

import java.net.URL;

/**
 * Created by anweshmishra on 01/08/15.
 */
public class RequestRunner implements Runnable {
    RequestFallback requestFallback;
    private boolean isRunning = true;
    public void run() {
        while(isRunning) {
            try {
                if(!RequestUtil.isServerDown(new URL(AppConfig.PROD_URL+AppConfig.APPOINTMENT_SERVICE))) {
                    requestFallback.addRequestsToQueue();
                }
                Thread.sleep(100);
            }
            catch (Exception exception) {

            }
        }
    }
    public void init() {
        isRunning = true;
    }
    public RequestRunner(RequestFallback requestFallback) {
        this.requestFallback = requestFallback;
    }
    public void stop() {
        isRunning  = false;
    }
}
