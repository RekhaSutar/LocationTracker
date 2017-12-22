package rekha.com.locationtracker.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import rekha.com.locationtracker.R
import rekha.com.locationtracker.view.userjourney.UserJourneyListActivity


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val mapFrag = fragmentManager.findFragmentById(R.id.map) as LocationFragment

        switchLocationTrack.setOnCheckedChangeListener { _, isChecked ->
            mapFrag.setUserMovementTrackingFlag(isChecked)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        startActivity(Intent(this, UserJourneyListActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    private var count: Int = 0
    override fun onBackPressed() {
        if (count == 0) {
            count = 1
            if (switchLocationTrack.isChecked) {
                showMessage("Press back again to stop tracking and close the app")
            } else {
                showMessage("Press back again to close the app")
            }
        } else {
            super.onBackPressed()
        }
    }
}
