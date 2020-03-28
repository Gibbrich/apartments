package com.github.gibbrich.airmee.core.repository

import androidx.lifecycle.LiveData
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.model.BookingRange

interface ApartmentsRepository {
    val cachedApartments: LiveData<List<Apartment>>
    suspend fun fetchApartments()
    fun bookApartment(apartmentId: Int, bookingRange: BookingRange)
}