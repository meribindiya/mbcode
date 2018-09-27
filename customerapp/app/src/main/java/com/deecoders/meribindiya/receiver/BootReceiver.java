package com.deecoders.meribindiya.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.deecoders.meribindiya.util.UserActiveService;
import com.deecoders.meribindiya.util.Utils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // Set the alarm here.
            Intent i = new Intent(context, UserActiveService.class);
            context.startService(i);

            Utils.setActiveUserAlarm(context);
        }
    }
}