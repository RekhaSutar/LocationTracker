package rekha.com.locationtracker.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import rekha.com.locationtracker.data.Location
import rekha.com.locationtracker.data.LocationViewModel
import rekha.com.locationtracker.db.UserJourney
import rekha.com.locationtracker.db.UserJourneyViewModel
import rekha.com.locationtracker.utility.checkLocationPermission


class LocationFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var userJourneyViewModel: UserJourneyViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var currentLocation: Location
    private lateinit var googleMap: GoogleMap
    private var isTrackingOn = false
    private var userMovements: MutableList<Location> = arrayListOf()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        locationViewModel = ViewModelProviders.of(activity).get(LocationViewModel::class.java)
        userJourneyViewModel = ViewModelProviders.of(activity).get(UserJourneyViewModel::class.java)

        val liveData = locationViewModel.getLocation(context!!)
        liveData.observe(this, Observer { newLocation ->
            if (isTrackingOn) {
                //whenever tracking is switched on current location and the new location both are added in the user journey
                // so always two journey will be there when tracking is switched on
                if (userMovements.size == 0) {
                    userMovements.add(this.currentLocation)
                }
                userMovements.add(newLocation!!)
            }
            this.currentLocation = newLocation!!
            updateLocation(newLocation!!)
        })
        getMapAsync(this)
    }

    private fun updateLocation(newLocation: Location) {
        if (!isTrackingOn) {
//            user is simply moved to new location
            val latLng = LatLng(newLocation.latitude, newLocation.longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } else {
//            drawn route on map from previous to new location
            if (userMovements.size > 0) {
                val previousLocationPosition = userMovements.size - 2
                val previousLocation = userMovements[previousLocationPosition]
                val previousLatLng = LatLng(previousLocation.latitude, previousLocation.longitude)
                val newLatLng = LatLng(newLocation.latitude, newLocation.longitude)
                if (userMovements.size == 2) {
                    googleMap.addMarker(MarkerOptions().position(previousLatLng).title("start currentLocation"))
                }
                googleMap.addPolyline(PolylineOptions().add(previousLatLng, newLatLng).width(2.0f).color(Color.BLUE).geodesic(true))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (checkLocationPermission(activity)) {
            this.googleMap.isMyLocationEnabled = true
        }
    }

    fun setUserMovementTrackingFlag(isTrackingOn: Boolean) {
        this.isTrackingOn = isTrackingOn
        if (!isTrackingOn) {
            //if off and if list of locations is not empty then store all locations and empty the user movements list
            userJourneyViewModel.storeUserJourney(UserJourney(userMovements))
            userMovements = arrayListOf()
        }
    }
}

