package com.deecoders.meribindiya.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.deecoders.meribindiya.R;
import com.devspark.appmsg.AppMsg;

public class BaseActivity extends AppCompatActivity {
    ViewGroup container;
    private FrameLayout contentframe;
    private LottieAnimationView animationview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        this.animationview = (LottieAnimationView) findViewById(R.id.animation_view);
        container = (ViewGroup)findViewById(R.id.container);
    }

    public void showSuccessToast(String msg){
        AppMsg.makeText(this, msg, AppMsg.STYLE_INFO).show();
    }

    public void showErrorToast(String msg){
        AppMsg.makeText(this, msg, AppMsg.STYLE_ALERT).show();
    }

    protected void replaceContentLayout(int sourceId) {
        View contentLayout = findViewById(R.id.content_frame);
        ViewGroup parent = (ViewGroup) contentLayout.getParent();
        int index = parent.indexOfChild(contentLayout);
        parent.removeView(contentLayout);
        contentLayout = getLayoutInflater().inflate(sourceId, parent, false);
        parent.addView(contentLayout, index);
    }

    public void showProgress() {
        animationview.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        animationview.setVisibility(View.INVISIBLE);
    }

    public void setCustomActionBar(int color, String title, boolean backArrow){
        ActionBar abar = getSupportActionBar();
        if(abar != null) {
            abar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(color)));
            View viewActionBar = getLayoutInflater().inflate(R.layout.customer_actionbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
            ImageView menuIcon = viewActionBar.findViewById(R.id.menu);
            textviewTitle.setText(title);
            if(color == R.color.white) {
                textviewTitle.setTextColor(getResources().getColor(R.color.black));
            }
            else{
                final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
                upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                abar.setHomeAsUpIndicator(upArrow);
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            abar.setCustomView(viewActionBar, params);
            abar.setDisplayShowCustomEnabled(true);
            abar.setDisplayShowTitleEnabled(false);
            if(backArrow)
                abar.setDisplayHomeAsUpEnabled(true);
            abar.setElevation(0);
        }
    }
 }
