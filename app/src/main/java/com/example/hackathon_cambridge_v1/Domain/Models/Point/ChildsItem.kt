package com.example.hackathon_cambridge_v1.Domain.Models.Point

import com.google.gson.annotations.SerializedName

data class ChildsItem(
    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)