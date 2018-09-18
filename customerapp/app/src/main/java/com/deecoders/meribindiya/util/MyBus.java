package com.deecoders.meribindiya.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class MyBus extends Bus {
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public void post(final Object event) {
        //Looper.prepare();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        }
        else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MyBus.super.post(event);
                }
            });
        }
    }
}
