package com.deecoders.meribindiya.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.deecoders.meribindiya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Congrats extends AppCompatActivity {

    @BindView(R.id.viewOrder)
    Button viewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @OnClick(R.id.viewOrder)
    public void onViewClicked() {
        Intent intent = new Intent(this, Bookings.class);
        startActivity(intent);
        finish();
    }
}
