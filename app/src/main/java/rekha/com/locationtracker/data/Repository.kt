package rekha.com.locationtracker.data

import android.arch.lifecycle.LiveData
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import rekha.com.locationtracker.MainApplication
import rekha.com.locationtracker.data.db.DataBase
import rekha.com.locationtracker.data.db.UserJourney
import rekha.com.locationtracker.service.LocationTrackingService

object Repository {

    private val TAG = "Repository"
    private var locationLiveData: LiveData<Location>? = null
    private var isTrackingOn = false
    private var userMovements: MutableList<Location> = arrayListOf()
    private var previousLocation: Location? = null
    private var appDatabase: DataBase = DataBase.getAppDatabase(MainApplication.getInstance())
    private var locationService: LocationTrackingService? = null

    fun getLocation(): LiveData<Location> {
        MainApplication.application.startService(Intent(MainApplication.getInstance(), LocationTrackingService::class.java))
        if (locationLiveData == null) {
            locationLiveData = LocationLiveData(Location(0.0, 0.0, 0f, 0))
        }
        return locationLiveData!!
    }

    fun getUserJourney(): List<Location> {
        return userMovements
    }

    fun updateLocation(location: Location) {
        Log.d("updateLocation", location.time.toString())

        if (isTrackingOn) {
            userMovements.add(location)
        }
        previousLocation = location
        this.locationLiveData = LocationLiveData(location)
    }

    fun setServiceInstance(locationService: LocationTrackingService) {
        this.locationService = locationService
    }

    fun stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates")
        locationService?.onDestroy()
    }

    fun startTracking() {
        isTrackingOn = true
        if (previousLocation != null && previousLocation!!.latitude != 0.0 && previousLocation!!.longitude != 0.0) {
            userMovements.add(previousLocation!!)
        }
    }

    fun stopTracking() {
        isTrackingOn = false
        StoreUserJourneyInBackGround(appDatabase).execute(UserJourney(userMovements))
//        clear user records
        userMovements = arrayListOf()
    }

    class StoreUserJourneyInBackGround(private val appDatabase: DataBase) : AsyncTask<UserJourney, Unit, Unit>() {
        override fun doInBackground(vararg userJourney: UserJourney?) {
            appDatabase.userJourneyDaoModel().addUserJourney(userJourney[0])
            return
        }
    }

}