package com.example.hackathon_cambridge_v1.Domain.Models.Point

import com.google.gson.annotations.SerializedName

data class FuelCategoryItem(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("childs")
    val childs: List<ChildsItem?>? = null
)
