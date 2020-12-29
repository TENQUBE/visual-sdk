package com.tenqube.visualsample.catcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tenqube.visualsample.visual.VisualManager;


public class SMSCatcher extends BroadcastReceiver {

    private String TAG = SMSCatcher.class.getSimpleName();

    private static final String SMS_TEST_ACTION = "ACTION_VISUAL_SMS_TEST";
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public SMSCatcher() {

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        VisualManager.getInstance(context).parseSms(intent, result ->
                Log.i("SmsParsing sms: ", result.toString()));
    }

}