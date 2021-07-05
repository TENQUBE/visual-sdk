package com.tenqube.visualsample;

import android.app.Application;
import android.content.Context;


import com.tenqube.visualsample.catcher.rcs.RcsCatcher;
import com.tenqube.visualsample.visual.VisualManager;

public class VisualSampleApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        VisualManager.getInstance(this).initialize();
        VisualManager.getInstance(this).parseRcs();


//        RcsCatcher.getInstance(this).register(); //rcs 등록
    }
}
