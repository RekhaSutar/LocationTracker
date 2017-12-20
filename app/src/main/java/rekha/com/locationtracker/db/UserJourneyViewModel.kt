package rekha.com.locationtracker.db

import android.arch.lifecycle.ViewModel
import rekha.com.locationtracker.MainApplication

class UserJourneyViewModel : ViewModel() {

    private var appDatabase: DataBase = DataBase.getAppDatabase(MainApplication.getInstance())

    fun getAllUserJourneys(): List<UserJourney>? {
        return appDatabase.userJourneyDaoModel().allUserJourneys
    }
}
