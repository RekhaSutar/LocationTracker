package rekha.com.locationtracker.data

import android.Manifest
import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Looper
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.*

class LocationLiveData(private val context: Context) : LiveData<Location>() {

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            return
        }
        val locationProviderClient = getFusedLocationProviderClient()
        val locationRequest = LocationRequest.create()
        val looper = Looper.myLooper()
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, looper)
    }

    private fun getFusedLocationProviderClient(): FusedLocationProviderClient {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        }
        return fusedLocationProviderClient!!
    }

//    override fun onInactive() {
//        if (fusedLocationProviderClient != null) {
//            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
//        }
//    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val newLocation = locationResult.lastLocation
            val latitude = newLocation.latitude
            val longitude = newLocation.longitude
            val accuracy = newLocation.accuracy
            val time = newLocation.time
            val location = Location(latitude, longitude, accuracy, time)
            value = location
        }
    }
}