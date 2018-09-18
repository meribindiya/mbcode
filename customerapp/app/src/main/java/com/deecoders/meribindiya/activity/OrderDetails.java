package com.deecoders.meribindiya.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.OrderAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.util.TinyDB;
import com.deecoders.meribindiya.view.ExpandableHeightGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetails extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.listView)
    ExpandableHeightGridView listView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    OrderAdapter orderAdapter;
    ArrayList<ProductModel> models = new ArrayList<>();
    @BindView(R.id.noresult)
    TextView noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                finish();
            }
        });

        TinyDB tinyDB = new TinyDB(this);
        models = tinyDB.getListObject("products", ProductModel.class);
        clearDirtyModels();
        orderAdapter = new OrderAdapter(this, models);
        listView.setAdapter(orderAdapter);
        listView.setExpanded(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 50);

        if(models.size()==0){
            noresult.setVisibility(View.VISIBLE);
        }
        else{
            noresult.setVisibility(View.GONE);
        }
    }

    private void clearDirtyModels() {
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getCount() == 0) {
                models.remove(i);
            }
        }
    }
}
