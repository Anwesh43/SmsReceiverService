package com.example.anweshmishra.smsreceiverservice;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

/**
 * Created by anweshmishra on 22/06/15.
 */
public class RequestUtil {
    public static StringRequest makeRequest(final Context context,final HashMap<String,String> params) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.PROD_URL, new Response.Listener<String>() {
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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
}
