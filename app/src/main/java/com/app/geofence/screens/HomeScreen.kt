package com.app.geofence.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.app.geofence.Screen
import com.app.geofence.utils.isLocationEnabled
import com.app.geofence.utils.isPermissionGranted
import com.app.geofence.utils.locationPermission
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MapViewModel = viewModel()
) {
    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            if (!isPermissionGranted(context, locationPermission())) {
                Toast.makeText(context, "Please grant location permission", Toast.LENGTH_SHORT)
                    .show()
                return@Button
            }
            if (!context.isLocationEnabled()) {
                Toast.makeText(context, "Please enable location", Toast.LENGTH_SHORT).show()
                return@Button
            }

            val geoClient = LocationServices.getGeofencingClient(context)

            val geofencePendingIntent = getPendingIntent(context)
            val geofences =
                listOf(
                    getGeofence(
                        "YOUR_GEOFENCE_ID",
                        viewModel.latitude,
                        viewModel.longitude,
                        viewModel.radius
                    )
                )

            val seekGeofencing = GeofencingRequest.Builder().apply {
                setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                addGeofences(geofences)
            }.build()

            geoClient.addGeofences(seekGeofencing, geofencePendingIntent).run {
                addOnSuccessListener {
//                    Toast.makeText(context, "Geofences added", Toast.LENGTH_SHORT).show()
                }
                addOnFailureListener {
//                    Toast.makeText(context, "Failed to add geofences", Toast.LENGTH_SHORT).show()
                    it.printStackTrace()
                }
            }
        }) {
            Text(text = "Start Geofence in background")
        }

        Button(onClick = {

            if (!isPermissionGranted(context, locationPermission())) {
                Toast.makeText(context, "Please grant location permission", Toast.LENGTH_SHORT)
                    .show()
                return@Button
            }

            if (!context.isLocationEnabled()) {
                Toast.makeText(context, "Please enable location", Toast.LENGTH_SHORT).show()
                return@Button
            }

            navController.navigate(Screen.MapScreen.route)
        }) {
            Text(text = "Start Geofence in Map")
        }
    }

}