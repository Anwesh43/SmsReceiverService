package com.example.anweshmishra.smsreceiverservice;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by anweshmishra on 22/06/15.
 */
public class RequestUtil {
    public static StringRequest makeRequest(final Context context,final HashMap<String,String> params,final String service) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.PROD_URL+service, new Response.Listener<String>() {
            public void onResponse(String response) {
                Random random = new Random();
                Toast.makeText(context, response+(random.nextInt(100)+30), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public HashMap<String,String> getParams() {

                return params;
            }
        };
        return stringRequest;
    }
    public static boolean isServerDown(URL url) {
        boolean serverDown = true;
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            serverDown = false;
        }
        catch(Exception exception) {

        }
        return serverDown;
    }
}
