package com.deecoders.meribindiya.util;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.constants.Constants;

import org.json.JSONObject;

public class UserActiveService extends IntentService {

    public UserActiveService() {
        super("UserActiveService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("tag","in onHandleIntent ");
        sendActiveUserPing();
    }

    private void sendActiveUserPing() {
        Log.d("tag","in sendActiveUserPing ");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.aliveUser
                + MyPref.getId(this), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", "sendActiveUserPing " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(req);
    }
}
