package com.deecoders.meribindiya.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.network.CustomRequest;
import com.deecoders.meribindiya.network.VolleyLibrary;
import com.devspark.appmsg.AppMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Otp extends AppCompatActivity {
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 351);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 351);
        }
    }

    public void submit(View view) {
        if (view != null)
            Constants.clickEffect(view);
        if(mobile.getText().toString().isEmpty())
            return;

        sendRequest();
    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        String url = Constants.loginUrl+mobile.getText().toString();
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("success", response.toString());
                progressBar.setVisibility(View.GONE);
                try {
                    String status = response.getString("status");
                    if(status.equals("success")){
                        String screen = response.getString("message");
                        if(screen.equals("OTP_SCREEN")){
                            JSONObject object = response.getJSONObject("object");
                            String otpCode = object.getString("otp");
                            Intent intent = new Intent(Otp.this, OtpVerification.class);
                            intent.putExtra("code", otpCode);
                            intent.putExtra("mobile", mobile.getText().toString());
                            startActivity(intent);
                        }
                        else if(screen.equals("REGISTRATION_SCREEN")){
                            Intent intent = new Intent(Otp.this, Signup.class);
                            intent.putExtra("mobile", mobile.getText().toString());
                            startActivity(intent);
                        }
                        finish();
                    }
                    else{
                        AppMsg.makeText(Otp.this, "Error!", AppMsg.STYLE_ALERT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Otp.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyLibrary.getInstance(this).addToRequestQueue(customRequest, "", false);
    }

}
