package com.github.gibbrich.airmee.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.model.ApartmentFilter
import com.github.gibbrich.airmee.core.model.BookingRange

interface ApartmentParametersRepository {
    val filter: LiveData<ApartmentFilter>

    fun changeBedsQuantity(isIncrease: Boolean)
    fun changeRange(bookingRange: BookingRange?)
    fun clearFilters()
}

class ApartmentParametersDataRepository : ApartmentParametersRepository {
    override val filter = MutableLiveData(ApartmentFilter())

    override fun changeBedsQuantity(isIncrease: Boolean) {
        val apartmentFilter = filter.value!!
        val newBedsQuantity = if (isIncrease) {
            apartmentFilter.beds.inc()
        } else {
            apartmentFilter.beds.dec()
        }
        filter.value = apartmentFilter.copy(beds = newBedsQuantity)
    }

    override fun changeRange(bookingRange: BookingRange?) {
        val apartmentFilter = filter.value!!
        filter.value = apartmentFilter.copy(bookingRange = bookingRange)
    }

    override fun clearFilters() {
        filter.value = ApartmentFilter()
    }
}