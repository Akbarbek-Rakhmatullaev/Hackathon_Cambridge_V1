package com.example.hackathon_cambridge_v1.Domain.Models.Points

import com.google.gson.annotations.SerializedName

data class PointsItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("coords")
    val coords: Coords? = null)