package com.app.geofence.screens

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.app.geofence.GeofenceNotificationReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: MapViewModel = viewModel()
) {

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(viewModel.latLong, 15f)
    }

    LaunchedEffect(Unit) {

        val geoClient = LocationServices.getGeofencingClient(context)

        val geofencePendingIntent = getPendingIntent(context)
        val geofences = listOf(
            getGeofence(
                "YOUR_GEOFENCE_ID",
                viewModel.latitude,
                viewModel.longitude,
                50f
            )
        )

        val seekGeofencing = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofences)
        }.build()

        geoClient.addGeofences(seekGeofencing, geofencePendingIntent).run {
            addOnSuccessListener {
                Toast.makeText(context, "Geofence added", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(context, "Failed to add geofence", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        }
    }

    val circleOptions = CircleOptions()
        .center(LatLng(viewModel.latitude, viewModel.longitude))
        .radius(50.0)

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true)
            ) {
                Circle(
                    center = circleOptions.center!!,
                    radius = circleOptions.radius,
                    fillColor = Color.Red.copy(alpha = 0.2f),
                    strokeWidth = 2f,
                    strokeColor = Color.Blue
                )
            }
        }
    }
}

fun getPendingIntent(context: Context): PendingIntent {

    val intent = Intent(context, GeofenceNotificationReceiver::class.java)

    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    } else {
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    return pendingIntent
}

fun getGeofence(requestId: String, latitude: Double, longitude: Double, radius: Float): Geofence {
    return Geofence.Builder()
        .setRequestId(requestId)
        .setCircularRegion(latitude, longitude, radius)
        .setExpirationDuration(Geofence.NEVER_EXPIRE)  // Optional expiration time
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        .build()
}