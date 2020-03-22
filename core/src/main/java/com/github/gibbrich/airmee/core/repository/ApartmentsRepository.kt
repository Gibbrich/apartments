package com.github.gibbrich.airmee.core.repository

import com.github.gibbrich.airmee.core.model.Apartment

interface ApartmentsRepository {
    suspend fun getApartments(): List<Apartment>
}