package rekha.com.locationtracker.view

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import rekha.com.locationtracker.R


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
}
