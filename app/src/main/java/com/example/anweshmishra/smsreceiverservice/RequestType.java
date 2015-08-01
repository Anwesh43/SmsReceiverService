package com.example.anweshmishra.smsreceiverservice;

/**
 * Created by anweshmishra on 30/07/15.
 */
public enum RequestType {
    APPOINTMENT("appointment"),CANCELLATION("cancellation");
    private String value;
    RequestType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
