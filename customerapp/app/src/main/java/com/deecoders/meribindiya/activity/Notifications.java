package com.deecoders.meribindiya.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.NotificationAdapter;
import com.deecoders.meribindiya.model.NotificationModel;
import com.deecoders.meribindiya.constants.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Notifications extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.listView)
    ListView listView;
    ArrayList<NotificationModel> models = new ArrayList<>();
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        fillDummy();
        notificationAdapter=new NotificationAdapter(this, models);
        listView.setAdapter(notificationAdapter);
    }

    private void fillDummy() {
        models.add(new NotificationModel());
        models.add(new NotificationModel());
        models.add(new NotificationModel());
        models.add(new NotificationModel());
        models.add(new NotificationModel());
        models.add(new NotificationModel());
        models.add(new NotificationModel());
        models.add(new NotificationModel());
        models.add(new NotificationModel());
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }
}
