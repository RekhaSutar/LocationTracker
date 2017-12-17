package rekha.com.locationtracker.utility

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import rekha.com.locationtracker.R

const val REQUEST_CODE_LOCATION_PERMISSION = 1

fun checkLocationPermission(activity: Activity) : Boolean{
    return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showPermissionExplanationDialog(activity)
        } else {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
        false
    }else{
        true
    }
}

fun showPermissionExplanationDialog(activity: Activity) {
    val builder = AlertDialog.Builder(activity)
    builder.setMessage(activity.resources.getString(R.string.location_permission_explanation_msg))
    builder.setPositiveButton("Next", { _, _ ->
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION);
    })
    builder.setOnDismissListener { dialog -> dialog.dismiss() }
    val alertDialog = builder.create()
    alertDialog.show()
}

fun checkIfGooglePlayServicesIsInstalled(context: Activity): Boolean {
    if (!isGooglePlayServicesAvailable(context)) {
        if (!isUserRecoverableGooglePlayServicesError(context)) {
            Toast.makeText(context, "Google Play Services must be installed.",
                    Toast.LENGTH_SHORT).show()
            context.finish()
        }
        return true
    }
    return false
}

fun isGooglePlayServicesAvailable(context: Context): Boolean {
    val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)

    return resultCode == ConnectionResult.SUCCESS
}

fun isUserRecoverableGooglePlayServicesError(context: Context): Boolean {
    return GooglePlayServicesUtil.isUserRecoverableError(GooglePlayServicesUtil.isGooglePlayServicesAvailable(context))
}

fun googlePlayErrorDialog(cancelListener: DialogInterface.OnCancelListener?, activity: Activity, requestCode: Int): Dialog {
    val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)
    return if (cancelListener == null)
        GooglePlayServicesUtil.getErrorDialog(resultCode, activity, requestCode)
    else
        GooglePlayServicesUtil.getErrorDialog(resultCode, activity, requestCode, cancelListener)
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isNetworkActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    val isGPSActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    return isNetworkActive or isGPSActive
}

