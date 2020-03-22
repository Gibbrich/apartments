package com.github.gibbrich.airmee.core.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    val locationSource: LiveData<Location>
    fun getLocation(): LatLng
    fun connect()
    fun disconnect()
}