package com.deecoders.meribindiya.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.util.MyPref;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(getIntent().hasExtra("paramm")){
            finish();
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MyPref.getLogin(Splash.this) == 1){
                    startActivity(new Intent(Splash.this, Home.class));
                    finish();
                }
                else{
                    startActivity(new Intent(Splash.this, Otp.class));
                    finish();
                }
            }
        }, 3000);
    }
}


