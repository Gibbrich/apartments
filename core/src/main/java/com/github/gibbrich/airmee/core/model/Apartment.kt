package com.github.gibbrich.airmee.core.model

data class Apartment(
    val id: Int,
    val bedrooms: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double
)