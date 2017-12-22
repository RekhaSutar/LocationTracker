package rekha.com.locationtracker.view.userjourney

import android.os.Bundle
import rekha.com.locationtracker.BuildConfig
import rekha.com.locationtracker.R
import rekha.com.locationtracker.view.BaseActivity

const val ARG_JOURNEY_ID = "journey_id"
class UserJourneyDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_journey_details)
        var journeyId = 0
        if (intent != null && intent.extras != null) {
            journeyId = intent.extras!!.getInt(ARG_JOURNEY_ID)
        } else {
            if (BuildConfig.DEBUG) {
                throw IllegalArgumentException("journeyId is missing")
            }
        }
        val fragmentManager = supportFragmentManager
        val mapFrag = fragmentManager.findFragmentById(R.id.map) as UserJourneyMap
        mapFrag.setJourneyId(journeyId)

    }
}
