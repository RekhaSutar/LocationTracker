package rekha.com.locationtracker.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context

class LocationViewModel : ViewModel() {
    private var locationLiveData: LiveData<Location>? = null

    fun getLocation(context: Context): LiveData<Location> {
        if (locationLiveData == null) {
            locationLiveData = LocationLiveData(context)
        }
        return locationLiveData!!
    }
}