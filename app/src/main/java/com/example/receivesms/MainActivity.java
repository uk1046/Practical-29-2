package com.example.receivesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView senderTextView;
    private TextView receivedMessageTextView;
    private BroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senderTextView = findViewById(R.id.senderTextView);
        receivedMessageTextView = findViewById(R.id.receivedMessageTextView);
        registerSmsReceiver();
    }

    private void registerSmsReceiver() {
        smsReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus != null) {
                        for (Object pdu : pdus) {
                            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, bundle.getString("format"));
                            String sender = smsMessage.getDisplayOriginatingAddress();
                            String message = smsMessage.getDisplayMessageBody();

                            // Display sender and message in TextViews
                            senderTextView.setText("From: " + sender);
                            receivedMessageTextView.setText("Message: " + message);
                        }
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }
}
