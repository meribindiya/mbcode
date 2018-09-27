package com.deecoders.meribindiya.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.util.MyPref;
import com.devspark.appmsg.AppMsg;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpVerification extends AppCompatActivity {
    @BindView(R.id.code)
    PinEntryEditText code;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.resend)
    TextView resend;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.mobile)
    TextView mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);

        //code.setText(getIntent().getStringExtra("code"));
        mobile.setText(""+getIntent().getStringExtra("mobile"));

        registerReceiver(smsReceiver, new IntentFilter("otp_sms"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }

    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code_ = intent.getStringExtra("code");
            if(code_ != null)
                code.setText(code_);
        }
    };

    public void verify(View view) {
        Constants.clickEffect(view);
        if (code.getText().toString().isEmpty()) {
            return;
        }
        sendRequest();
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    public void resend(View view) {
        Constants.clickEffect(view);
    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("otp", code.getText().toString());
            object.put("mobile", mobile.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.userOtpVerification, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", ""+response.toString());
                        progressBar.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                JSONObject user = response.getJSONObject("object");
                                String uid = user.getString("user_id");
                                String email = user.getString("email");
                                String mobile = user.getString("mobile");
                                String gender = user.getString("gender");
                                String name = user.getString("name");
                                MyPref.setId(OtpVerification.this, uid);
                                MyPref.setEmail(OtpVerification.this, email);
                                MyPref.setMobile(OtpVerification.this, mobile);
                                MyPref.setGender(OtpVerification.this, gender);
                                MyPref.setName(OtpVerification.this, name);
                                MyPref.setLogin(OtpVerification.this, 1);
                                Intent intent = new Intent(OtpVerification.this, Home.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                AppMsg.makeText(OtpVerification.this, ""+msg, AppMsg.STYLE_ALERT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OtpVerification.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }
}
