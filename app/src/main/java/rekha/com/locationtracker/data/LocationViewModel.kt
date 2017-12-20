package rekha.com.locationtracker.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class LocationViewModel : ViewModel() {

    private var repository: RepositoryImpl = RepositoryImpl

    fun getLocation(): LiveData<Location> {
        return repository.getLocation()
    }

    fun startTracking(){
        repository.startTracking()
    }

    fun stopTracking(){
        repository.stopTracking()
    }

    fun stopLocationUpdates(){
        repository.stopLocationUpdates()
    }
}