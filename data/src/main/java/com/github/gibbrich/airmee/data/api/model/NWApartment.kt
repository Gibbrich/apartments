package com.github.gibbrich.airmee.data.api.model

import com.google.gson.annotations.SerializedName

class NWApartment(
    @SerializedName("id")
    val id: String,

    @SerializedName("bedrooms")
    val bedrooms: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)