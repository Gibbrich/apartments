package com.github.gibbrich.airmee.core.repository

import com.github.gibbrich.airmee.core.model.Apartment

interface ApartmentsRepository {
    val cachedApartments: List<Apartment>
    suspend fun fetchApartments(): List<Apartment>
}