package com.example.hackathon_cambridge_v1.Domain.Models.Points

import com.google.gson.annotations.SerializedName

data class Coords(
    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null)
