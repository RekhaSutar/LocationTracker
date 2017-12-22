package rekha.com.locationtracker.data.db

import android.arch.lifecycle.ViewModel
import rekha.com.locationtracker.MainApplication

class UserJourneyViewModel : ViewModel() {

    private var appDatabase: DataBase = DataBase.getAppDatabase(MainApplication.getInstance())

    fun getAllUserJourneys(): List<UserJourney>? {
        return appDatabase.userJourneyDaoModel().allUserJourneys
    }

    fun getJourney(journeyId : Int): UserJourney? {
        return appDatabase.userJourneyDaoModel().getUserJourney(journeyId)
    }
}
