package com.deecoders.meribindiya.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.CategoryAdapter;
import com.deecoders.meribindiya.adapter.SliderAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.fragment.MainMenu;
import com.deecoders.meribindiya.listeners.ServiceDataListener;
import com.deecoders.meribindiya.model.CategoryModel;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.model.SliderModel;
import com.deecoders.meribindiya.model.UserModel;
import com.deecoders.meribindiya.util.LocationHelper;
import com.deecoders.meribindiya.util.MyPref;
import com.deecoders.meribindiya.util.TinyDB;
import com.deecoders.meribindiya.util.Utils;
import com.deecoders.meribindiya.view.ExpandableHeightGridView;
import com.devspark.appmsg.AppMsg;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity {
    @BindView(R.id.gridView)
    GridView gridView;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryModel> models = new ArrayList<>();
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.menu)
    ImageView menu;
    ArrayList<SliderModel> sliderModels = new ArrayList<>();
    SliderAdapter sliderAdapter;
    @BindView(R.id.actionbar_textview)
    TextView title;
    @BindView(R.id.titleBar)
    RelativeLayout titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    MainMenu left_drawer;


    Handler handler;
    Runnable runnable;
    int sliderPos = 0;
    @BindView(R.id.location)
    ImageView location;

    private LocationHelper locationHelper;
    private boolean locationNeeded = false;
    private boolean isLocationMandatory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        left_drawer = (MainMenu)getSupportFragmentManager().findFragmentById(R.id.left_drawer);
        left_drawer.setServiceDataListener(serviceDataListener);

        DrawableCompat.setTint(menu.getDrawable(), ContextCompat.getColor(this, R.color.white));
        DrawableCompat.setTint(location.getDrawable(), ContextCompat.getColor(this, R.color.white));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                drawerToggle();
            }
        });

        getBanners();
        getCategories();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // Showing status
        if (status != ConnectionResult.SUCCESS) {
            // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }

        /*MyPref.setId(this, "1");
        MyPref.setMobile(this, "9876543210");
        MyPref.setEmail(this, "ffsdf@gdg.com");
        MyPref.setName(this, "myname");
        MyPref.setGender(this, "male");*/
        sendFcmRequest();

        Log.e("tag", "user_id: " + MyPref.getId(this));
        Log.e("tag", "date: "+new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
        sendActiveUserPing();
        Utils.setActiveUserAlarm(this);
        Utils.enableBootReceiver(this);


        locationHelper = new LocationHelper(this, new ServiceDataListener<String>() {
            @Override
            public void onData(String data) {
                updateLocationInDB(data);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationNeeded)
            locationHelper.onResume(isLocationMandatory);
    }

    private void getBanners() {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.banners, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        progressBar.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("object");
                                Type listType = new TypeToken<ArrayList<SliderModel>>() {
                                }.getType();
                                ArrayList<SliderModel> modelsNew = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
                                sliderModels.addAll(modelsNew);
                                sliderAdapter = new SliderAdapter(Home.this, sliderModels);
                                final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(sliderAdapter);

                                final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(Home.this) {
                                    @Override
                                    protected int getVerticalSnapPreference() {
                                        return LinearSmoothScroller.SNAP_TO_START;
                                    }
                                };

                                handler = new Handler();
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (progressBar == null)
                                            return;

                                        smoothScroller.setTargetPosition(sliderPos);
                                        mLayoutManager.startSmoothScroll(smoothScroller);
                                        //mLayoutManager.scrollToPositionWithOffset(sliderPos, 0);
                                        sliderPos++;
                                        if (sliderPos >= sliderModels.size()) {
                                            sliderPos = 0;
                                        }
                                        handler.postDelayed(runnable, 3000);
                                    }
                                };
                                handler.postDelayed(runnable, 3000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Home.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void getCategories() {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.getCategories, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        progressBar.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("object");
                                Type listType = new TypeToken<ArrayList<CategoryModel>>() {
                                }.getType();
                                ArrayList<CategoryModel> modelsNew = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
                                models.addAll(modelsNew);

                                categoryAdapter = new CategoryAdapter(Home.this, models);
                                gridView.setAdapter(categoryAdapter);
                                //gridView.setExpanded(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Home.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    boolean open;

    public void openDrawer() {
        open = true;
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        open = false;
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void drawerToggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    private void sendFcmRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("fcm", FirebaseInstanceId.getInstance().getToken());
            object.put("mobile", Long.parseLong(MyPref.getMobile(this)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("tag", "sending: " + object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.userFcm, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "fcm: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(Home.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void sendActiveUserPing() {
        Log.d("tag","in sendActiveUserPing ");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.aliveUser
                + MyPref.getId(this), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", "sendActiveUserPing " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCart();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        locationHelper = null;
    }

    private void clearCart() {
        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putListObject("products", new ArrayList<ProductModel>());
    }

    @OnClick ({R.id.location, R.id.actionbar_textview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location:
            case R.id.actionbar_textview:
                locationNeeded = true;
                isLocationMandatory = false;
                locationHelper.onResume(isLocationMandatory);
                break;
            default:
                locationNeeded = false;
        }
    }
    private ServiceDataListener<UserModel> serviceDataListener = new ServiceDataListener<UserModel>() {
        @Override
        public void onData(UserModel data) {
            title.setText(data.getLocation());
            if (data.getLocation() == null || data.getLocation() == "") {
                isLocationMandatory = true;
                locationHelper.onResume(isLocationMandatory);
                locationNeeded = true;
                title.setText("updating..");
            } else {
                locationNeeded = false;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void updateLocationInDB (final String location) {
        Log.d("tag","in updateLocationInDB ");
        title.setText("updating..");
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("location", location);
            object.put("mobile", MyPref.getMobile(this));
            object.put("email", MyPref.getEmail(this));
            object.put("name", MyPref.getName(this));
            object.put("gender", MyPref.getGender(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.userUpdate, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                isLocationMandatory = false;
                                locationNeeded = false;
                                title.setText(location);
                                Toast.makeText(Home.this, "Location Updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                AppMsg.makeText(Home.this, "" + msg, AppMsg.STYLE_ALERT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Home.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

}
