package com.tenqube.visualsample;

import android.app.Application;
import android.util.Log;

public class VisualSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("migration", "oncreate");


//        RcsCatcher.getInstance(this).register(); //rcs 등록
    }
}
