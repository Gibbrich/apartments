package com.github.gibbrich.airmee.model

import com.google.android.gms.maps.model.LatLng

data class CameraProperties(
    val latLng: LatLng,
    val zoom: Float = DEFAULT_ZOOM,
    val shouldMove: Boolean = true
) {
    companion object {
        private const val DEFAULT_ZOOM = 15f
    }
}