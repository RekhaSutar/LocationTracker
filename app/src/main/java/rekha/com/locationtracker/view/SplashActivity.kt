package rekha.com.locationtracker.view

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import rekha.com.locationtracker.R


const val REQUEST_CODE_LOCATION_PERMISSION = 1
fun checkLocationPermission(activity: Activity) {
    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showPermissionExplanationDialog(activity)
        } else {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }
}

fun showPermissionExplanationDialog(activity: Activity) {
    val builder = AlertDialog.Builder(activity)
    builder.setMessage(activity.resources.getString(R.string.location_permission_explanation_msg))
    builder.setPositiveButton("Next", { _,_ ->
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION);
    })
    builder.setOnDismissListener { dialog -> dialog.dismiss() }
    val alertDialog = builder.create()
    alertDialog.show()
}

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Check for location permission
        checkLocationPermission(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    showMessage("permission granted")
                } else {
                    showMessage("Please give permission to use this app")
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
}
