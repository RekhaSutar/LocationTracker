package rekha.com.locationtracker.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


class LocationDataProvider{

    private val context: Context
    private var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequest = LocationRequest.create()
    private val looper = Looper.myLooper()

    private lateinit var locationCallback: LocationCallback

    constructor(context: Context, locationCallback: LocationCallback) {
        this.context = context
        this.locationCallback = locationCallback
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, looper)
        }
    }

    fun removeLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}
