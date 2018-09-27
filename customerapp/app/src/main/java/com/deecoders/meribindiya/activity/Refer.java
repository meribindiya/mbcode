package com.deecoders.meribindiya.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.UserModel;
import com.deecoders.meribindiya.util.MyPref;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Refer extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;

    @BindView(R.id.referNowBtn)
    Button referNowBtn;
    @BindView(R.id.beautyNetworkBtn)
    Button beautyNetworkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        referNowBtn.setOnClickListener(this);
        beautyNetworkBtn.setOnClickListener(this);
        initViews ();
    }

    private void initViews() {
        UserModel userModel = MyPref.getProfile(this);
        if(userModel.isBeautyNetworkActivated()){
            beautyNetworkBtn.setVisibility(View.VISIBLE);
        } else {
            beautyNetworkBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }


    @Override
    public void onClick(View view) {
        Constants.clickEffect(view);
        switch (view.getId()) {
            case R.id.referNowBtn:
                Intent intent = new Intent(Refer.this, ShareContact.class);
                startActivity(intent);
                break;
            case R.id.beautyNetworkBtn:
                Intent intent1 = new Intent(Refer.this, NetworkDetails.class);
                startActivity(intent1);
                break;
        }
    }
}
