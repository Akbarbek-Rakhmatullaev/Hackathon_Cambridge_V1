package com.example.hackathon_cambridge_v1.Domain.Models.Point

import com.google.gson.annotations.SerializedName

data class Mon(
    @field:SerializedName("from")
    val from: String? = null,

    @field:SerializedName("to")
    val to: String? = null
)
{}