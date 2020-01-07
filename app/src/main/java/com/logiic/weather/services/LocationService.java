package com.logiic.weather.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

public abstract class LocationService implements LocationListener {

    private final static boolean FORCE_NETWORK = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final String TAG = "LocationService";

    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;

    private static LocationService instance = null;

    private boolean isForceNetwork;
    private boolean isGPSEnabled;
    private  boolean isNetworkEnabled;
    private LocationManager locationManager;
    private boolean locationServiceAvailable;

    public Location location;
    public double longitude;
    public double latitude;

    private LocationService( Context context ) {
        initLocationService(context);
    }

    public static LocationService getLocationManager(Context context) {
        if (instance == null) {
            instance = new LocationService(context) {
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        }

        return instance;
    }

    /**
    * Sets up location service after permissions is granted
    */
    @TargetApi(23)
    private void initLocationService(Context context) {


        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (FORCE_NETWORK) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled) {
                this.locationServiceAvailable = false;
            }

            this.locationServiceAvailable = true;

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        } catch (Exception ex)  {
            Log.d(TAG, "Error creating location service: " + ex.getMessage());

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //
    }
}
