package rekha.com.locationtracker.view.userjourney

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import rekha.com.locationtracker.data.db.UserJourney
import rekha.com.locationtracker.data.db.UserJourneyViewModel
import com.google.android.gms.maps.model.MarkerOptions




class UserJourneyMap : SupportMapFragment(), OnMapReadyCallback {

    private val TAG = "UserJourneyMap"
    private var journeyId: Int = 0
    private var userJourneyViewModel: UserJourneyViewModel? = null
    private var googleMap: GoogleMap? = null

    fun setJourneyId(journeyId: Int){
        this.journeyId = journeyId
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        drawUserJourneyOnMap()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        userJourneyViewModel = ViewModelProviders.of(this).get(UserJourneyViewModel::class.java)
        getMapAsync(this)
    }

    private fun drawUserJourneyOnMap() {
        GetUserJourneyInBackGround(userJourneyViewModel!!, journeyId, googleMap!!).execute()

    }

    class GetUserJourneyInBackGround(private val userJourneyViewModel: UserJourneyViewModel,
                                     private val journeyId: Int,
                                     private val googleMap: GoogleMap):
            AsyncTask<Unit, UserJourney, UserJourney>(){
        override fun doInBackground(vararg params: Unit?): UserJourney? {
            return userJourneyViewModel?.getJourney(journeyId)

        }

        override fun onPostExecute(journey: UserJourney) {
            if (journey != null) {
                if (journey.user_journey.isNotEmpty()) {
                    val listLatLng = arrayListOf<LatLng>()
                    journey.user_journey.map {
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
                    if (listLatLng.size > 2) {
                        val startPoint = listLatLng[0]
                        googleMap.addMarker(MarkerOptions().position(startPoint)
                                .title("Start point :${journey.user_journey[0].time}"))
                        val endPoint = listLatLng[listLatLng.size - 1]
                        googleMap.addMarker(MarkerOptions().position(endPoint)
                                .title("End point : ${journey.user_journey[listLatLng.size - 1].time}"))
                    }
                }
            }
        }
    }
}
