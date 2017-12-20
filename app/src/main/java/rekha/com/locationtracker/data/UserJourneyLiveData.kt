package rekha.com.locationtracker.data

import android.arch.lifecycle.LiveData

class UserJourneyLiveData : LiveData<List<Location>> {

    constructor(userJourney: List<Location>) : super() {
        value = userJourney
    }

}