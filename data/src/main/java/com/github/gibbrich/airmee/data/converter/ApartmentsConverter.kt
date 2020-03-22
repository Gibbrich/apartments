package com.github.gibbrich.airmee.data.converter

import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.data.api.model.NWApartment

object ApartmentsConverter {
    fun fromNetwork(data: NWApartment): Apartment = Apartment(
        Integer.decode(data.id),
        data.bedrooms,
        data.name,
        data.latitude,
        data.longitude
    )
}