package com.github.gibbrich.airmee.data.repository

import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.data.api.Api
import com.github.gibbrich.airmee.data.converter.ApartmentsConverter

/**
 * For simplicity, apartments data requests only once, on map screen open.
 */
class ApartmentsDataRepository(
    private val api: Api
) : ApartmentsRepository {
    override val cachedApartments = MutableLiveData<List<Apartment>>()

    override suspend fun fetchApartments() {
        cachedApartments.value = api.getApartments().map(ApartmentsConverter::fromNetwork)
    }

    override fun bookApartment(apartmentId: Int, bookingRange: BookingRange) {
        cachedApartments.value
            ?.find { it.id == apartmentId }
            ?.bookingRange = bookingRange
        cachedApartments.value = cachedApartments.value
    }
}