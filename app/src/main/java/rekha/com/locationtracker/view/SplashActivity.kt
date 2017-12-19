package rekha.com.locationtracker.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import rekha.com.locationtracker.R
import rekha.com.locationtracker.utility.REQUEST_CODE_LOCATION_PERMISSION
import rekha.com.locationtracker.utility.checkIfGooglePlayServicesIsInstalled
import rekha.com.locationtracker.utility.checkLocationPermission
import rekha.com.locationtracker.utility.isLocationEnabled


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (checkIfGooglePlayServicesIsInstalled(this)) return

        if (!isLocationEnabled(this)) {
            promptToEnableGPS()
        } else {
            //Check for location permission
            if (checkLocationPermission(this)) {
                moveToNextScreen()
            }
        }
    }

    private fun moveToNextScreen(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    moveToNextScreen()
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(resources.getString(R.string.location_permission_explanation_msg))
                    builder.setPositiveButton("Ok") { _, _ -> finish() }
                    builder.setOnDismissListener { finish() }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
                return
            }
        }
    }

    private fun promptToEnableGPS() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(resources.getString(R.string.location_enable_msg))
        builder.setPositiveButton("Go to Settings") { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        builder.setOnDismissListener { finish() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

}
