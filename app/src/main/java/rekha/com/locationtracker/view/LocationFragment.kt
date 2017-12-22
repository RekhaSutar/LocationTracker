package rekha.com.locationtracker.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import rekha.com.locationtracker.data.Location
import rekha.com.locationtracker.data.LocationViewModel
import rekha.com.locationtracker.data.Repository
import rekha.com.locationtracker.utility.checkLocationPermission


class LocationFragment : SupportMapFragment(), OnMapReadyCallback {
    private val TAG = "LocationFragment"
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var currentLocation: Location
    private var googleMap: GoogleMap? = null
    private var isTrackingOn = false
    private lateinit var baseActivity : BaseActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        locationViewModel = ViewModelProviders.of(activity).get(LocationViewModel::class.java)
        getMapAsync(this)
    }

    private fun startLocationUpdates() {
        val liveData = locationViewModel.getLocation()
        liveData.observe(this, Observer { newLocation ->
            updateLocation(newLocation!!)
        })
    }

    private fun updateLocation(newLocation: Location) {
        if (isTrackingOn) {
            showUserPathOnMap()
        } else {
            if (newLocation.latitude != 0.0 && newLocation.longitude != 0.0) {
                Log.d(TAG, "inside if")
                val newLatLng = LatLng(newLocation.latitude, newLocation.longitude)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 5f))
            }

            Log.d(TAG, "outside if")
        }
        this.currentLocation = newLocation

        Log.d(TAG, "updateLocation" + "lat:" + newLocation.latitude + " lng:" + newLocation.longitude)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (checkLocationPermission(activity)) {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationChangeListener { location ->
                Log.d(TAG, "first time location setting")
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude,
                        location.longitude), 5f))
                googleMap.setOnMyLocationChangeListener(null)

            }
        }
    }

    fun setUserMovementTrackingFlag(isTrackingOn: Boolean) {
        this.isTrackingOn = isTrackingOn
        if (isTrackingOn) {
            locationViewModel.startTracking()
        } else {
            if (locationViewModel.getUserJourney().isNotEmpty() && locationViewModel.getUserJourney().size > 1) {
                (activity as BaseActivity).showMessage("Your tracked journey is saved")
            }
            locationViewModel.stopTracking()
            googleMap?.clear()
        }
    }


    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
        if (!isTrackingOn) {
            locationViewModel.stopLocationUpdates()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        if (isTrackingOn) {
            showUserPathOnMap()
        } else {
            startLocationUpdates()
        }
    }

    private fun showUserPathOnMap() {
        googleMap?.clear()

        Log.e(TAG, "showUserPathOnMap" + " LocationTrackingService")
        if (locationViewModel.getUserJourney().isNotEmpty()) {
            val listLatLng = arrayListOf<LatLng>()
            locationViewModel.getUserJourney().map {
                listLatLng.add(LatLng(it.latitude, it.longitude))
            }
            val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
            options.addAll(listLatLng)
            googleMap?.addPolyline(options)
            val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(listLatLng[listLatLng.size - 1].latitude, listLatLng[listLatLng.size - 1].longitude))
                    .zoom(5f)
                    .build()
            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
}

