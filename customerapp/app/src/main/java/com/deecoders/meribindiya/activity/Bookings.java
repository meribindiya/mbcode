package com.deecoders.meribindiya.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.BookingAdapter;
import com.deecoders.meribindiya.adapter.BookingsPagerAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.BookingModel;
import com.deecoders.meribindiya.util.MyPref;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Bookings extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    public static ArrayList<BookingModel> models = new ArrayList<>();
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    BookingsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        /*BookingModel model1=new BookingModel();
        model1.setOrderstatus(Constants.newOrder);
        BookingModel model2=new BookingModel();
        model2.setOrderstatus(Constants.newOrder);
        timeSlotModels.add(model1);
        timeSlotModels.add(model2);

        pagerAdapter=new BookingsPagerAdapter(this, getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        slidingTabs.setupWithViewPager(viewpager);*/

        getBookingsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        models.clear();
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    private void getBookingsList() {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.allOrders + "/" + MyPref.getId(this), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        if (progressBar == null)
                            return;
                        progressBar.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("object");
                                Type listType = new TypeToken<ArrayList<BookingModel>>() {}.getType();
                                ArrayList<BookingModel> modelsNew = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
                                models.addAll(modelsNew);
                                pagerAdapter=new BookingsPagerAdapter(Bookings.this, getSupportFragmentManager());
                                viewpager.setAdapter(pagerAdapter);
                                slidingTabs.setupWithViewPager(viewpager);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                if (progressBar == null)
                    return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Bookings.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(Bookings.this).add(req);
    }

}
