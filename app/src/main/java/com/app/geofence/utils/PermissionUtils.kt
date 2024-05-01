package com.app.geofence.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat

fun locationPermission(): String {
    var locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    if (Build.VERSION.SDK_INT >= 29) {
        locationPermission = Manifest.permission.ACCESS_BACKGROUND_LOCATION
    }
    return locationPermission
}

fun isPermissionGranted(context: Context, permission: String): Boolean =
    ActivityCompat.checkSelfPermission(
        context, permission
    ) == PackageManager.PERMISSION_GRANTED

fun Activity.isLocationEnabled(): Boolean {
    val locationManager: LocationManager =
        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}