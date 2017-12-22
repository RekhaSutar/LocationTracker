package rekha.com.locationtracker.view.userjourney

import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_user_journey_list.*
import rekha.com.locationtracker.R
import rekha.com.locationtracker.data.db.UserJourney
import rekha.com.locationtracker.data.db.UserJourneyViewModel
import rekha.com.locationtracker.view.BaseActivity


class UserJourneyListActivity : BaseActivity() {

    private lateinit var userJourneyViewModel: UserJourneyViewModel
    private lateinit var adapter: UserJourneyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_journey_list)

        adapter = UserJourneyListAdapter(this)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        userJourneyViewModel = ViewModelProviders.of(this).get(UserJourneyViewModel::class.java)
        GetUserJourneysInBackGround(userJourneyViewModel, adapter).execute()
    }

    class GetUserJourneysInBackGround(private val userJourneyViewModel: UserJourneyViewModel, private val adapter: UserJourneyListAdapter):
            AsyncTask<Unit, List<UserJourney>, List<UserJourney>>(){
        override fun doInBackground(vararg params: Unit?): List<UserJourney>? {
            return userJourneyViewModel.getAllUserJourneys()
        }

        override fun onPostExecute(result: List<UserJourney>?) {
            if (result != null) {
                adapter.addUserJourneyList(result)
            }
        }
    }
}
