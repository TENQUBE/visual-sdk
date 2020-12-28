package com.tenqube.visualsample;

import android.app.Application;

import com.tenqube.visualsample.catcher.rcs.RcsCatcher;
import com.tenqube.visualsample.visual.VisualManager;

public class VisualSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VisualManager.getInstance(this).initialize();

        RcsCatcher.getInstance(this).register(); //rcs 등록
    }
}
