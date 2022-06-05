package com.example.hackathon_cambridge_v1.Domain.Models.Points

import com.google.gson.annotations.SerializedName

data class WebPoints(
    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("items")
    val items: List<PointsItem?>? = null)