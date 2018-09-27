package com.deecoders.meribindiya.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.deecoders.meribindiya.activity.Splash;

/**
 * Created by saif on 3/27/2018.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.e("tag", "onReceive");
        Intent scheduledIntent = new Intent(context, Splash.class);
        scheduledIntent.putExtra("paramm", "paramm");
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);
    }
}
