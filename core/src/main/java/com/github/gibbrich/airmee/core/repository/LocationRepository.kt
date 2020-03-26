package com.github.gibbrich.airmee.core.repository

import android.location.Location
import androidx.lifecycle.LiveData

interface LocationRepository {
    val locationSource: LiveData<Location>
    fun getLocation(): Location
    fun connect()
    fun disconnect()
}