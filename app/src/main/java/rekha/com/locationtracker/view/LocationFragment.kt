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
import rekha.com.locationtracker.utility.checkLocationPermission

class LocationFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var googleMap : GoogleMap
    private lateinit var location: Location
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val locationViewModel = ViewModelProviders.of(activity).get(LocationViewModel::class.java)

        val liveData = locationViewModel.getLocation(context!!)
        liveData.observe(this, Observer {
            location -> updateLocation(location!!) })

        getMapAsync(this)
    }

    fun updateLocation(location: Location) {
        if (googleMap != null){
            val latLng = LatLng(location.latitude, location.longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (checkLocationPermission(activity)){
            this.googleMap.isMyLocationEnabled = true
        }
    }
}
