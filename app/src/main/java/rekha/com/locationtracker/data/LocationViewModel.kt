package rekha.com.locationtracker.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class LocationViewModel : ViewModel() {

    private var repository: Repository = Repository

    fun getLocation(): LiveData<Location> {
        return repository.getLocation()
    }

    fun getUserJourney(): List<Location> {
        return repository.getUserJourney()
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