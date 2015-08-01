package com.example.anweshmishra.smsreceiverservice;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

/**
 * Created by anweshmishra on 31/07/15.
 */
public class LargeRetryPolicy implements RetryPolicy{
    public int getCurrentTimeout() {
        return 10000;
    }
    public int getCurrentRetryCount() {
        return 100;
    }
    public void retry(VolleyError error)throws VolleyError {

    }
}
