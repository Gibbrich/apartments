package com.github.gibbrich.airmee.data.repository

import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.data.api.Api
import com.github.gibbrich.airmee.data.converter.ApartmentsConverter
import com.github.gibbrich.airmee.data.db.AppDatabase

class ApartmentsDataRepository(
    private val api: Api,
    private val db: AppDatabase
) : ApartmentsRepository {
    override val cachedApartments = MutableLiveData<List<Apartment>>(mutableListOf())

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