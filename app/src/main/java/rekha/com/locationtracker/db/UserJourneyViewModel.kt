package rekha.com.locationtracker.db

import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import rekha.com.locationtracker.MainApplication


class UserJourneyViewModel : ViewModel() {

    private var appDatabase: DataBase = DataBase.getAppDatabase(MainApplication.getInstance())

    fun storeUserJourney(userJourney: UserJourney){
        StoreUserJourneyInBackGround(appDatabase).execute(userJourney)
    }

    class StoreUserJourneyInBackGround(private val appDatabase: DataBase):AsyncTask<UserJourney, Unit, Unit>(){
        override fun doInBackground(vararg userJourney: UserJourney?): Unit {
            appDatabase.userJourneyDaoModel().addUserJourney(userJourney[0])
            return
        }
    }
}
