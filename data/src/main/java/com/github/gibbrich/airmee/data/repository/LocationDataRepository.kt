package com.github.gibbrich.airmee.data.repository

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.utils.isLocationPermissionGranted
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class LocationDataRepository(private val context: Context) : LocationRepository {
    companion object {
        private val DEFAULT_LOCATION = Location("").apply {
            latitude = 59.329440
            longitude = 18.069124
        }
    }

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest().apply {
        interval = 10000
        fastestInterval = 10000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    override val locationSource = MutableLiveData(DEFAULT_LOCATION)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.locations.lastOrNull() ?: return
            locationSource.value = location
        }
    }

    override fun connect() {
        if (isLocationPermissionGranted(context)) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun disconnect() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}