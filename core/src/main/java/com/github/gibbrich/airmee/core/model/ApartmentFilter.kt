package com.github.gibbrich.airmee.core.model

data class ApartmentFilter(
    val beds: Int = 0,
    val bookingRange: BookingRange? = null
)