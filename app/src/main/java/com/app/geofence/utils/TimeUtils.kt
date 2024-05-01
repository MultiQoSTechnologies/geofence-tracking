package com.app.geofence.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


var geofenceEnterTime: Long = 0
var geofenceExitTime: Long = 0

fun convertMillisToTime(millis: Long): String {
    val date = Date(millis)

    val formatter = SimpleDateFormat("HH:mm:ss a", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()

    return formatter.format(date)
}

fun covertTimeToText(): String? {
    var convTime: String? = null
    try {
        val dateDiff: Long = geofenceExitTime - geofenceEnterTime

        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff) % 60
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff) % 60
        val hours: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)

        if (hours > 0) {
            convTime = "$hours hours "
            if (minutes > 0) {
                convTime += "$minutes minutes "
            }
        } else if (minutes > 0) {
            convTime = "$minutes minutes "
            if (seconds > 0) {
                convTime += "$seconds seconds"
            }
        } else {
            convTime = "$seconds seconds "
        }

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return convTime
}