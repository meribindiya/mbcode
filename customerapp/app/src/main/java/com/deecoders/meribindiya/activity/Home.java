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
import com.deecoders.meribindiya.model.CategoryModel;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.model.SliderModel;
import com.deecoders.meribindiya.util.MyPref;
import com.deecoders.meribindiya.util.TinyDB;
import com.deecoders.meribindiya.view.ExpandableHeightGridView;
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
    Handler handler;
    Runnable runnable;
    int sliderPos = 0;
    @BindView(R.id.location)
    ImageView location;
    Handler handlerLoc;
    Runnable runnableLoc;
    LocationManager locationManager;
    boolean locationUpdates;

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

        DrawableCompat.setTint(menu.getDrawable(), ContextCompat.getColor(this, R.color.white));
        DrawableCompat.setTint(location.getDrawable(), ContextCompat.getColor(this, R.color.white));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                drawerToggle();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 111);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 112);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            loadLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 111);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 112);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCart();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if(handlerLoc != null){
            handlerLoc.removeCallbacks(runnableLoc);
        }
    }

    private void clearCart() {
        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putListObject("products", new ArrayList<ProductModel>());
    }

    private void showGPSDisabledAlertToUser() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("GPS Alert");
        dialog.setMessage("Please enable your GPS!");
        dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void loadLocation() {
        Location location = getLastKnownLocation();
        if (location != null) {
            new GetLocationAsync(location.getLatitude(), location.getLongitude()).execute();
        } else {
            Log.e("tag", "location null");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("tag", "permission not granted");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 351);
                return;
            }
            if (!locationUpdates) {
                Log.e("tag", "start service");
                boolean isPassiveEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSEnabled)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                else if (isNetworkEnabled)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
                else if (isPassiveEnabled)
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, mLocationListener);
                else
                    Log.e("tag", "cannot start service");
                startLocationSchedular();
            }
            locationUpdates = true;
        }
    }

    private void startLocationSchedular() {
        handlerLoc = new Handler();
        runnableLoc = new Runnable() {
            @Override
            public void run() {
                Location location = getLastKnownLocation();
                if(location != null) {
                    Log.e("tag", "run success");
                    new GetLocationAsync(location.getLatitude(), location.getLongitude()).execute();
                    return;
                }
                handlerLoc.postDelayed(runnableLoc, 1000);
            }
        };
        handlerLoc.postDelayed(runnableLoc, 1000);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.e("tag", "new location");
            new GetLocationAsync(location.getLatitude(), location.getLongitude()).execute();
            if(locationManager != null)
                locationManager.removeUpdates(mLocationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private Location getLastKnownLocation() {
        if (locationManager == null)
            return null;
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            bestLocation = locationManager.getLastKnownLocation(provider);
            if (bestLocation != null) {
                break;
            }
        }
        return bestLocation;
    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            x = latitude;
            y = longitude;
            Log.e("tag", "lat: "+latitude);
            Log.e("tag", "lang: "+longitude);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Geocoder geocoder = new Geocoder(Home.this, Locale.ENGLISH);
                List<Address> addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (Geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);
                    //String one = returnAddress.getAddressLine(0);
                    String one = returnAddress.getLocality()+", "+returnAddress.getAdminArea();
                    String two = returnAddress.getAddressLine(1);
                    String three = returnAddress.getAddressLine(2);
                    if (three == null) {
                        three = "";
                    }
                    str.append(one);
                    return str.toString();
                } else {
                    Log.e("tag", "geocoder error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("tag", "geocoder error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String add) {
            if(title == null)
                return;
            if(add == null)
                return;

            try {
                title.setText(add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
