package rekha.com.locationtracker.data;

import android.arch.lifecycle.MutableLiveData;
import android.location.*;
import android.location.Location;

public class LocationModule {
    private MutableLiveData<android.location.Location> currentLocation;

    public MutableLiveData<Location> getCurrentLocation() {

        return currentLocation;
    }
}
