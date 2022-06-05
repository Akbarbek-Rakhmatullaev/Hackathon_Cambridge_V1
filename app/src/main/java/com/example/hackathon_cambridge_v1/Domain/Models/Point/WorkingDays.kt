package com.example.hackathon_cambridge_v1.Domain.Models.Point

import com.google.gson.annotations.SerializedName

data class WorkingDays(

    @field:SerializedName("thu")
    val thu: Thu? = null,

    @field:SerializedName("tue")
    val tue: Tue? = null,

    @field:SerializedName("wed")
    val wed: Wed? = null,

    @field:SerializedName("sat")
    val sat: Sat? = null,

    @field:SerializedName("fri")
    val fri: Fri? = null,

    @field:SerializedName("mon")
    val mon: Mon? = null,

    @field:SerializedName("sun")
    val sun: Sun? = null)
