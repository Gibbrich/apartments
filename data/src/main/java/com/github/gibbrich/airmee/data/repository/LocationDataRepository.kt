package com.github.gibbrich.airmee.data.repository

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.checkLocationPermission
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class LocationDataRepository(private val context: Context): LocationRepository {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest().apply {
        interval = 10000
        fastestInterval = 10000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    override val locationSource = MutableLiveData(
        Location("").apply {
            latitude = 59.329440
            longitude = 18.069124
        }
    )

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.locations.lastOrNull() ?: return
            locationSource.value = location
        }
    }

    override fun connect() {
        if (checkLocationPermission(context)) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        // todo - request permissions
    }

    override fun disconnect() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}