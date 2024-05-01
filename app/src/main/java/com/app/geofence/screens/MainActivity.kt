package com.app.geofence.screens

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.app.geofence.AppNavigation
import com.app.geofence.ui.theme.GeofenceTheme
import com.app.geofence.utils.AlertView
import com.app.geofence.utils.PermissionAlertView
import com.app.geofence.utils.createChannel
import com.app.geofence.utils.openDialog
import com.app.geofence.utils.showPermissionAlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createChannel(this)

        setContent {
            GeofenceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }

                if (openDialog.value) {
                    AlertView()
                }

                if (showPermissionAlertDialog.value) {
                    PermissionAlertView()
                }
            }
        }
        checkPermissions()

    }

    private fun checkPermissions() {

        val permissionList = if (Build.VERSION.SDK_INT >= 29) {
            mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        Dexter.withContext(this)
            .withPermissions(permissionList)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let { rep ->

                        if (!rep.areAllPermissionsGranted()) {
                            showPermissionAlertDialog.value = true
                        }

                        if (rep.isAnyPermissionPermanentlyDenied) {
                            showPermissionAlertDialog.value = true
                        }
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?, token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }
}