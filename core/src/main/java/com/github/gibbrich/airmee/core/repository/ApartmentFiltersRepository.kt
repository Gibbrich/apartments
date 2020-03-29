package com.github.gibbrich.airmee.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.model.ApartmentFilter
import com.github.gibbrich.airmee.core.model.BookingRange

/**
 * Apartment list filtering by date will be performed only if [ApartmentFilter.bookingRange] in
 * [ApartmentFiltersRepository] is not null.
 */
interface ApartmentFiltersRepository {
    /**
     * Source of [ApartmentFilter]. Always not null.
     * Default item - [ApartmentFilter] with default constructor parameters
     */
    val filter: LiveData<ApartmentFilter>

    /**
     * Update [ApartmentFilter.beds] and emits new value of [ApartmentFiltersRepository.filter]
     * @param isIncrease increments/decrements number of beds
     */
    fun changeBedsQuantity(isIncrease: Boolean)

    /**
     * Update [ApartmentFilter.bookingRange] and emits new value of [ApartmentFiltersRepository.filter]
     * @param bookingRange null if user cleared filtering by date
     */
    fun changeRange(bookingRange: BookingRange?)

    /**
     * Update [ApartmentFiltersRepository.filter] value with [ApartmentFilter] with default constructor parameters
     */
    fun clearFilters()
}

class ApartmentFiltersDataRepository : ApartmentFiltersRepository {
    override val filter = MutableLiveData(ApartmentFilter())

    override fun changeBedsQuantity(isIncrease: Boolean) {
        val apartmentFilter = filter.value!!
        val beds = if (isIncrease) {
            apartmentFilter.beds.inc()
        } else {
            apartmentFilter.beds.dec()
        }

        val result = beds.coerceAtLeast(ApartmentFilter.MIN_BEDS_QUANTITY)

        filter.value = apartmentFilter.copy(beds = result)
    }

    override fun changeRange(bookingRange: BookingRange?) {
        val apartmentFilter = filter.value!!
        filter.value = apartmentFilter.copy(bookingRange = bookingRange)
    }

    override fun clearFilters() {
        filter.value = ApartmentFilter()
    }
}