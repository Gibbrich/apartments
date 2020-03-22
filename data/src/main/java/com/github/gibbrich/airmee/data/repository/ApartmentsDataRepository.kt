package com.github.gibbrich.airmee.data.repository

import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.data.api.Api
import com.github.gibbrich.airmee.data.converter.ApartmentsConverter
import com.github.gibbrich.airmee.data.db.AppDatabase

class ApartmentsDataRepository(
    private val api: Api,
    private val db: AppDatabase
): ApartmentsRepository {
    override suspend fun getApartments(): List<Apartment> = api.getApartments().map(ApartmentsConverter::fromNetwork)
}