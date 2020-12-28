package com.tenqube.visualsample.catcher;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.tenqube.visualsample.visual.VisualManager;


public class NotiCatcher extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public IBinder onBind(Intent mIntent) {
        return super.onBind(mIntent);
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        return super.onUnbind(mIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        VisualManager.getInstance(getApplicationContext()).parseNoti(sbn, result -> Log.i("SmsParsing noti: ", result.toString()));
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
    }

}


