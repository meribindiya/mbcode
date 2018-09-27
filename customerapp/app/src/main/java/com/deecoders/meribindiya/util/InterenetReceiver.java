package com.deecoders.meribindiya.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by saif on 8/3/2017.
 */

public class InterenetReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(!checkInternet(context)) {
            Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkInternet(Context context) {
        if (isNetworkAvailable(context)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
