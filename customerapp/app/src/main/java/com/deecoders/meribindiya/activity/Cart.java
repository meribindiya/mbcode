package com.deecoders.meribindiya.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.Checksum;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.CartAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.network.CustomRequest;
import com.deecoders.meribindiya.network.VolleyLibrary;
import com.deecoders.meribindiya.util.MyPref;
import com.deecoders.meribindiya.util.TinyDB;
import com.deecoders.meribindiya.view.ExpandableHeightGridView;
import com.google.gson.GsonBuilder;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Cart extends AppCompatActivity {
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
    ArrayList<ProductModel> models = new ArrayList<>();
    CartAdapter cartAdapter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Checksum checksum;
    JSONObject checksumObject;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.arrowRight)
    ImageView arrowRight;
    @BindView(R.id.checkout)
    LinearLayout checkout;
    @BindView(R.id.cartPanel)
    LinearLayout cartPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        DrawableCompat.setTint(arrowRight.getDrawable(), ContextCompat.getColor(this, R.color.white));

        TinyDB tinyDB = new TinyDB(this);
        models = tinyDB.getListObject("products", ProductModel.class);
        clearDirtyModels();
        cartAdapter = new CartAdapter(this, models);
        listView.setAdapter(cartAdapter);
        listView.setExpanded(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 50);

        showServicesCount();

        registerReceiver(AddToCartReceiver, new IntentFilter("cart"));
    }

    private void clearDirtyModels() {
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getCount() == 0) {
                models.remove(i);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(AddToCartReceiver);
    }

    private BroadcastReceiver AddToCartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("tag", "onReceive");
            TinyDB tinyDB = new TinyDB(context);
            models = tinyDB.getListObject("products", ProductModel.class);
            showServicesCount();
        }
    };

    private void showServicesCount() {
        int itemCount = 0, priceCount = 0;
        for (ProductModel model : models) {
            itemCount = itemCount + model.getCount();
            priceCount = priceCount + (model.getCount() * model.getPrice());
        }
        price.setText("" + priceCount + " INR");
        if (models.size() == 0) {
            checkout.setAlpha(0.5f);
        } else {
            checkout.setAlpha(1f);
        }
    }

    public void checkout(View view) {
        Constants.clickEffect(view);
        if (models.size() == 0) {
            checkout.setAlpha(0.5f);
            return;
        }

        int priceCount = 0;
        for (ProductModel model : models) {
            priceCount = priceCount + (model.getCount() * model.getPrice());
        }
        if (priceCount < 800) {
            Toast.makeText(Cart.this, "Minimum order limit is 800 INR!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, SelectDateTime.class);
        startActivity(intent);
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }
}
