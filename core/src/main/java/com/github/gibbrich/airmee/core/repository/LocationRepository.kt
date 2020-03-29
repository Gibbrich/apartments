package com.github.gibbrich.airmee.core.repository

import android.location.Location
import androidx.lifecycle.LiveData

interface LocationRepository {
    /**
     * Source of [Location]. Always not null.
     * Default item - location with lat: 59.32944, lon:18.069124
     */
    val locationSource: LiveData<Location>

    /**
     * Start observe [Location] updates if there is permission for ACCESS_FINE_LOCATION
     */
    fun connect()

    /**
     * Stop observe [Location] updates
     */
    fun disconnect()
}