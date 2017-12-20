package rekha.com.locationtracker.data

import android.arch.lifecycle.LiveData
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import rekha.com.locationtracker.MainApplication
import rekha.com.locationtracker.db.DataBase
import rekha.com.locationtracker.db.UserJourney
import rekha.com.locationtracker.service.LocationTrackingService

object RepositoryImpl : Repository {

    private val TAG = "RepositoryImpl"
    private var locationLiveData: LiveData<Location>? = null
    private var isTrackingOn = false
    private var userMovements: MutableList<Location> = arrayListOf()
    private var userJourneyLiveData: LiveData<List<Location>>? = null
    private var appDatabase: DataBase = DataBase.getAppDatabase(MainApplication.getInstance())
    private var locationService: LocationTrackingService? = null

    fun getLocation(): LiveData<Location> {

        Log.d(TAG, "getLocation" + " called")
        MainApplication.application.startService(Intent(MainApplication.getInstance(),LocationTrackingService::class.java))
        if (locationLiveData == null) {
            locationLiveData = LocationLiveData(Location(0.0, 0.0, 0f, 0))
        }
        return locationLiveData!!
    }

    fun getUserJourney(): LiveData<List<Location>>{
        userJourneyLiveData = UserJourneyLiveData(userMovements)
        return userJourneyLiveData as UserJourneyLiveData
    }

    fun updateLocation(location: Location) {
        this.locationLiveData = LocationLiveData(location)
        if (isTrackingOn) {
            userMovements.add(location)
        }
    }

    fun setServiceInstance(locationService : LocationTrackingService){
        this.locationService = locationService
    }

    fun stopLocationUpdates(){
        Log.d(TAG, "stopLocationUpdates" + " called")
//        locationService?.stopSelf()
    }

    fun startTracking() {
        isTrackingOn = true
    }

    fun stopTracking() {
        isTrackingOn = false
        StoreUserJourneyInBackGround(appDatabase).execute(UserJourney(userMovements))
    }

    class StoreUserJourneyInBackGround(private val appDatabase: DataBase): AsyncTask<UserJourney, Unit, Unit>(){
        override fun doInBackground(vararg userJourney: UserJourney?) {
            appDatabase.userJourneyDaoModel().addUserJourney(userJourney[0])
            return
        }
    }

}