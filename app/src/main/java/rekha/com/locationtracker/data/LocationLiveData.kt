package rekha.com.locationtracker.data

import android.arch.lifecycle.LiveData

class LocationLiveData : LiveData<Location> {

    constructor(location: Location?) : super() {
        value = location
    }

}