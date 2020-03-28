package com.github.gibbrich.airmee.core.model

data class ApartmentFilter(
    val beds: Int = MIN_BEDS_QUANTITY,
    val bookingRange: BookingRange? = null
) {
    companion object {
        const val MIN_BEDS_QUANTITY = 1
    }
}