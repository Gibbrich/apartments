package com.github.gibbrich.airmee.core.repository

import androidx.lifecycle.LiveData
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.model.BookingRange

interface ApartmentsRepository {
    /**
     * Source of [Apartment], which were retrieved from backend
     */
    val cachedApartments: LiveData<List<Apartment>>

    /**
     * Retrieve [Apartment] from backend
     */
    suspend fun fetchApartments()

    /**
     * Book [Apartment] with specified id and [BookingRange].
     * Triggers [ApartmentsRepository.cachedApartments] emission.
     */
    fun bookApartment(apartmentId: Int, bookingRange: BookingRange)
}