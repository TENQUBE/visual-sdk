package com.tenqube.visualsample.util

import android.os.Handler
import android.os.Looper

fun runOnMainThread(function: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        function.invoke()
    }
}