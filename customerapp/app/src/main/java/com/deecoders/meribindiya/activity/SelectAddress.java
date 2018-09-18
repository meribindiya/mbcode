package com.deecoders.meribindiya.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.fragment.MyMapFragment;
import com.devspark.appmsg.AppMsg;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectAddress extends AppCompatActivity {
    MyMapFragment mapFragment;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    private static boolean cameraFlag = true;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.save)
    Button save;
    private LatLng pLatLng;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 351);
        }
        else {
            setGoogleMap();
        }
    }

    private void setGoogleMap() {
        mapFragment = (MyMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {
                        Log.e("tag", "position: " + position);
                        if (cameraFlag) {
                            pLatLng = position.target;
                            new GetLocationAsync(pLatLng.latitude, pLatLng.longitude).execute();
                        }
                    }
                });
                if (ActivityCompat.checkSelfPermission(SelectAddress.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SelectAddress.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                Location myLocation = null;
                if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    myLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if(myLocation != null){
                    pLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pLatLng, 10), 1000, null);
                }
                else{
                    pLatLng = new LatLng(20.595164, 78.963606);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pLatLng, 6), 1000, null);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 351);
        }
        else {
            setGoogleMap();
        }
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    public void saveLocation(View view) {
        Constants.clickEffect(view);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("address", address.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void searchLocation(View view) {
        Constants.clickEffect(view);
        Intent intent = new Intent(this, SearchAddress.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                String address = data.getStringExtra("address");
                Log.e("tag", "address: "+address);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("address", address);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    // to get LatLng
    private LatLng getLatLng(String address) {
        Geocoder coder = new Geocoder(SelectAddress.this);
        List<Address> add;
        LatLng latlng = null;
        try {
            add = coder.getFromLocationName(address, 5);
            if (add == null) {
                return null;
            }
            Address location = add.get(0);
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
            AppMsg.makeText(this, "Geocoder service not responding.", AppMsg.STYLE_ALERT).show();
        }
        return latlng;
    }

    // to show location address
    private class GetLocationAsync extends AsyncTask<String, Void, String> {
        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;
        public GetLocationAsync(double latitude, double longitude) {
            x = latitude;
            y = longitude;
        }
        @Override
        protected void onPreExecute() {
            address.setText("Loading...");
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Geocoder geocoder = new Geocoder(SelectAddress.this, Locale.ENGLISH);
                List<Address> addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (Geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);
                    String one = returnAddress.getAddressLine(0);
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
            try {
                address.setText(add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
