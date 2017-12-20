package rekha.com.locationtracker.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import rekha.com.locationtracker.data.Location
import rekha.com.locationtracker.data.LocationViewModel
import rekha.com.locationtracker.utility.checkLocationPermission


class LocationFragment : SupportMapFragment(), OnMapReadyCallback {
    private val TAG = "LocationFragment"
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var currentLocation: Location
    private var googleMap: GoogleMap? = null
    private var isTrackingOn = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        locationViewModel = ViewModelProviders.of(activity).get(LocationViewModel::class.java)
        getMapAsync(this)
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        val liveData = locationViewModel.getLocation()
        liveData.observe(this, Observer { newLocation ->
            updateLocation(newLocation!!)
        })
    }

    private fun updateLocation(newLocation: Location) {

        Log.d(TAG, "updateLocation" + " called")
        val newLatLng = LatLng(newLocation.latitude, newLocation.longitude)
        if (isTrackingOn && currentLocation != null) {
            val previousLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            googleMap?.addPolyline(PolylineOptions().add(previousLatLng, newLatLng).width(2.0f).color(Color.BLUE).geodesic(true))
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 10f))
        } else {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 10f))
        }
        this.currentLocation = newLocation
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (checkLocationPermission(activity)) {
            googleMap.isMyLocationEnabled = true
        }
    }

    fun setUserMovementTrackingFlag(isTrackingOn: Boolean) {
        this.isTrackingOn = isTrackingOn
        if (isTrackingOn) {
            locationViewModel.startTracking()
        } else {
            locationViewModel.stopTracking()
        }
    }

    override fun onPause() {

        Log.d(TAG, "onPause" + " called")
        super.onPause()
        if (!isTrackingOn) {
            locationViewModel.stopLocationUpdates()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume" + " called")
        startLocationUpdates()
    }

}

