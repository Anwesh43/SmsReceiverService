package com.example.anweshmishra.smsreceiverservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.android.volley.*;

public class ServiceControllerActivity extends Activity implements Button.OnClickListener{
    Intent serviceIntent;
    BroadcastReceiver receiver;
    IntentFilter smsIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_controller);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        serviceIntent = new Intent(this,ReceiverService.class);
        smsIntentFilter = new IntentFilter();
        smsIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        Button startButton = (Button)findViewById(R.id.start);
        Button stopButton = (Button)findViewById(R.id.stop);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.start:
                //registerReceiver(receiver,smsIntentFilter);
                startService(serviceIntent);
                break;
            case R.id.stop:
                //unregisterReceiver(receiver);
                stopService(serviceIntent);
                break;
            default:
                break;
        }
    }
}
