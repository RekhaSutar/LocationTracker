package rekha.com.locationtracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import rekha.com.locationtracker.data.Location;
import rekha.com.locationtracker.data.LocationDataProvider;
import rekha.com.locationtracker.data.RepositoryImpl;

public class LocationTrackingService extends Service {

    private String TAG = "LocationTrackingService";
    private RepositoryImpl repository = RepositoryImpl.INSTANCE;
    private LocationDataProvider locationDataProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate" + " called");
        repository.setServiceInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationDataProvider = new LocationDataProvider(this,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        Log.d(TAG, "onLocationResult" + " called");
                        android.location.Location newLocation = locationResult.getLastLocation();
                        repository.updateLocation(new Location(newLocation.getLatitude(),
                                newLocation.getLongitude(), newLocation.getAccuracy(), newLocation.getTime()));
                    }
                });
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
