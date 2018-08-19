package com.meribindiyaemployee.meribindiyaemployee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.meribindiyaemployee.meribindiyaemployee.utils.PreferenceHandler;

public class LogoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceHandler.clearAll(this);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
