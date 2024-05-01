package com.app.geofence.screens

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel: ViewModel() {

    val latitude = 23.048492
    val longitude = 72.524782
    val radius = 50f

    val latLong = LatLng(latitude, longitude)
}