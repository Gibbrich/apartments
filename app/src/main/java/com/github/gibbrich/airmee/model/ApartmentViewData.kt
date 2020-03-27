package com.github.gibbrich.airmee.model

data class ApartmentViewData(
    val beds: Int,
    val name: String,
    val distanceToUserKm: Float,
    val latitude: Double,
    val longitude: Double
)