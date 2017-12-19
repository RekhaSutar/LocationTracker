package rekha.com.locationtracker.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import rekha.com.locationtracker.data.Location
import rekha.com.locationtracker.data.LocationViewModel
import rekha.com.locationtracker.db.UserJourney
import rekha.com.locationtracker.db.UserJourneyViewModel
import rekha.com.locationtracker.utility.checkLocationPermission

class LocationFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var location: Location
    private var isTrackingOn = false

    private var userMovements: MutableList<Location> = arrayListOf()

    private lateinit var userJourneyViewModel : UserJourneyViewModel
    private lateinit var locationViewModel : LocationViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        locationViewModel = ViewModelProviders.of(activity).get(LocationViewModel::class.java)
        userJourneyViewModel = ViewModelProviders.of(activity).get(UserJourneyViewModel::class.java)

        val liveData = locationViewModel.getLocation(context!!)
        liveData.observe(this, Observer { location ->
            updateLocation(location!!)
            if (isTrackingOn) {
                userMovements.add(location)
            }
        })
        getMapAsync(this)
    }

    private fun updateLocation(location: Location) {
        if (googleMap != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (checkLocationPermission(activity)) {
            this.googleMap.isMyLocationEnabled = true
        }
//        if (location != null) {
//            updateLocation(location)
//        }
    }

    fun setUserMovementTrackingFlag(isTrackingOn: Boolean) {
        this.isTrackingOn = isTrackingOn
        if (!isTrackingOn){
            //if off and if list of locations is not empty then store all locations and empty the user movements list
            userJourneyViewModel.storeUserJourney(UserJourney(userMovements))
            userMovements = arrayListOf()
        }
    }
}

