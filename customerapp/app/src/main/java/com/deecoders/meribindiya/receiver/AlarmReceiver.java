
package com.deecoders.meribindiya.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.deecoders.meribindiya.util.UserActiveService;
import com.deecoders.meribindiya.util.Utils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tag","in AlarmReceiver onReceive ");
        Intent i = new Intent(context, UserActiveService.class);
        context.startService(i);

    }
}
