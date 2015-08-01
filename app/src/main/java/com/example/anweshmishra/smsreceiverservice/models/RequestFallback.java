package com.example.anweshmishra.smsreceiverservice.models;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

/**
 * Created by anweshmishra on 01/08/15.
 */
public class RequestFallback {
    RequestQueue requestQueue;
    boolean isLocked = false;
    boolean isUploadingProcessInProgress= false;
    public RequestFallback(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }
    private ArrayList<StringRequest> stringRequests = new ArrayList<StringRequest>();
    public synchronized void addRequest(StringRequest stringRequest) throws Exception{
        lock();
        stringRequests.add(stringRequest);
        unlock();
    }
    public synchronized void addRequestsToQueue() throws Exception{
        lock();
        if(!isUploadingProcessInProgress && stringRequests.size() > 0) {
            isUploadingProcessInProgress = true;
            for (StringRequest stringRequest : stringRequests) {
                requestQueue.add(stringRequest);
            }
            stringRequests = new ArrayList<StringRequest>();
            isUploadingProcessInProgress = false;
        }
        unlock();
    }
    private void lock() throws Exception{
        while(isLocked) {
            wait();
        }
        isLocked = true;
    }
    private void unlock() throws Exception {
        isLocked = false;
        notifyAll();

    }
    public void init() {
        isUploadingProcessInProgress = false;
        stringRequests = new ArrayList<StringRequest>();
    }

}
