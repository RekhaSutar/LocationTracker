package rekha.com.locationtracker.data

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Looper
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.*

class LocationLiveData : LiveData<Location> {

    private val context: Context
    private var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequest = LocationRequest.create()
    private val looper = Looper.myLooper()
    private val locationCallback: LocationCallback

    constructor(context: Context) : super() {
        this.context = context
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        this.locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val newLocation = locationResult.lastLocation
                setValueToLiveData(newLocation.latitude, newLocation.longitude, newLocation.accuracy, newLocation.time)
            }
        }
    }

    private fun setValueToLiveData(latitude: Double, longitude: Double, accuracy: Float, time: Long) {
        val location = Location(latitude, longitude, accuracy, time)
        value = location
    }

    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            if (fusedLocationProviderClient.lastLocation.isComplete) {
                setValueToLiveData(fusedLocationProviderClient.lastLocation.result.latitude,
                        fusedLocationProviderClient.lastLocation.result.longitude,
                        fusedLocationProviderClient.lastLocation.result.accuracy,
                        fusedLocationProviderClient.lastLocation.result.time)
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, looper)
        }
    }

    override fun onInactive() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

}