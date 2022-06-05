package com.example.hackathon_cambridge_v1.Domain.Models.Point

import com.example.hackathon_cambridge_v1.Domain.Models.Points.Coords
import com.google.gson.annotations.SerializedName

data class PointItem(
    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("working_days")
    val workingDays: WorkingDays? = null,

    @field:SerializedName("company_name")
    val companyName: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("fuel_selection")
    val fuelSelection: ArrayList<FuelCategoryItem?>? = ArrayList(),

    @field:SerializedName("is_working")
    val isWorking: Boolean? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("coords")
    val coords: Coords? = null)
{}