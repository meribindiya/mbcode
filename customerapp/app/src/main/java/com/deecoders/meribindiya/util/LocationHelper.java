package com.deecoders.meribindiya.util;

import android.Manifest;
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.deecoders.meribindiya.listeners.ServiceDataListener;

import java.util.List;
import java.util.Locale;

public class LocationHelper {
    AppCompatActivity context;
    LocationManager locationManager;
    boolean locationUpdates = false;
    int LOCATION_REQUEST_CODE = 351;
    boolean permissionDenied = false;

    private ServiceDataListener<String> serviceDataListener;
    private boolean isMandatory;

    public LocationHelper (AppCompatActivity context, ServiceDataListener<String> serviceDataListener) {
        this.context = context;
        this.serviceDataListener = serviceDataListener;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void onResume (boolean isMandatory) {
        this.isMandatory = isMandatory;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (permissionDenied) {
                showPermissionMandatoryAlert ();
            } else {
                loadLocation();
            }
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("GPS Alert");
        dialog.setMessage("Please enable your GPS!");
        dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(callGPSSettingIntent);
                dialogInterface.dismiss();
            }
        });
        if (isMandatory) {
            dialog.setCancelable(false);
            dialog.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    exitApp();
                }
            });
        }
        dialog.show();
    }

    private void showPermissionMandatoryAlert () {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Permission Denied");
        dialog.setMessage("Location is mandatory to mark your location. Please allow us to read it.");

        dialog.setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    loadLocation();
                } else {
                    showGPSDisabledAlertToUser ();
                }
            }
        });
        if (isMandatory) {
            dialog.setCancelable(false);
            dialog.setNegativeButton("EXIT ANYWAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    exitApp();
                }
            });
        }
        dialog.show();
    }

    private void exitApp () {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(homeIntent);
    }

    private Location getLastKnownLocation() {
        if (locationManager == null)
            return null;
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            bestLocation = locationManager.getLastKnownLocation(provider);
            if (bestLocation != null) {
                break;
            }
        }
        return bestLocation;
    }

    private void loadLocation() {
        Location location = getLastKnownLocation();
        if (location != null) {
            new GetLocationAsync(location.getLatitude(), location.getLongitude()).execute();
        } else {
            Log.e("tag", "location null");
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("tag", "permission not granted");
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
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
                //startLocationSchedular();
            }
            locationUpdates = true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionDenied = true;
            } else {
                permissionDenied = false;
            }
        }
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
                Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
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
            if(add == null)
                return;
            try {
                serviceDataListener.onData(add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
