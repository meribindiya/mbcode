package com.deecoders.meribindiya.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Kunwar's on 27-Jan-17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.e(REG_TOKEN, recent_token);

//        sharedPreferences = getSharedPreferences(Util.Pref,MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        editor.putString(Util.Token,recent_token);
//        editor.commit();

    }
}
