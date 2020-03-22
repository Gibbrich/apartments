package com.github.gibbrich.airmee.data.api

import com.github.gibbrich.airmee.data.api.model.NWApartment
import retrofit2.http.GET

interface Api {
    @GET("apartments.json")
    suspend fun getApartments(): List<NWApartment>
}