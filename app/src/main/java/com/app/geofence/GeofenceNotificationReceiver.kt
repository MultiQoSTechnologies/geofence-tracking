package com.app.geofence

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.app.geofence.utils.covertTimeToText
import com.app.geofence.utils.geofenceEnterTime
import com.app.geofence.utils.geofenceExitTime
import com.app.geofence.utils.openDialog
import com.app.geofence.utils.sendGeofenceEnteredNotification
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceNotificationReceiver : BroadcastReceiver() {

    private val logTag = this::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(logTag, errorMessage)
            return
        }

        when (val geofenceTransition = geofencingEvent?.geofenceTransition) {
            GEOFENCE_TRANSITION_ENTER -> {
                geofenceEnterTime = System.currentTimeMillis()
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendGeofenceEnteredNotification(
                    context,
                    "You have entered in a geofence area"
                )
            }

            GEOFENCE_TRANSITION_EXIT -> {

                geofenceExitTime = System.currentTimeMillis()
                openDialog.value = true

                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.sendGeofenceEnteredNotification(
                    context,
                    "You stayed for ${covertTimeToText()} in the geofence area"
                )
            }

            else -> {
                Log.e(logTag, "Invalid type transition $geofenceTransition")
            }
        }
    }
}