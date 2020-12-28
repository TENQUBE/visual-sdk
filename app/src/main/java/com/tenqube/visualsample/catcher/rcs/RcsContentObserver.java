package com.tenqube.visualsample.catcher.rcs;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.tenqube.visualsample.visual.VisualManager;


class RcsContentObserver extends ContentObserver {

    private VisualManager visualManager;

    public RcsContentObserver(VisualManager visualManager, Handler handler) {
        super(handler);
        this.visualManager = visualManager;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        visualManager.parseRcs(uri, result -> Log.i("SmsParsing rcs: ", result.toString()));
    }

}