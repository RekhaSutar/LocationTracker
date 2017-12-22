package rekha.com.locationtracker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import rekha.com.locationtracker.data.Location;
import rekha.com.locationtracker.data.Repository;

public class LocationTrackingService extends Service {

    private String TAG = "LocationTrackingService";
    private Repository repository = Repository.INSTANCE;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 3f;

    LocationListener[] locationListeners;

    class LocationListener implements android.location.LocationListener {

        LocationListener(String provider) {
            Log.d(TAG, "LocationListener ");
            android.location.Location location = new android.location.Location(provider);
            repository.updateLocation(new Location(location.getLatitude(),
                    location.getLongitude(), location.getAccuracy(), location.getTime()));
        }

        @Override
        public void onLocationChanged(android.location.Location location) {

            Log.d(TAG, "onLocationChanged ");
            repository.updateLocation(new Location(location.getLatitude(),
                    location.getLongitude(), location.getAccuracy(), location.getTime()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    }


    @Override
    public void onCreate() {
        initializeLocationManager();
        setGpsProvider();
    }

    public void setGpsProvider() {
        this.locationListeners = new LocationListener[]{new LocationListener(LocationManager.GPS_PROVIDER)};
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    locationListeners[0]
            );
            android.location.Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
            repository.updateLocation(new Location(location.getLatitude(),
                    location.getLongitude(), location.getAccuracy(), location.getTime()));
            }

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < locationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(locationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }
}
