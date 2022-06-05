package com.example.hackathon_cambridge_v1.Data.Serializers

import com.example.hackathon_cambridge_v1.Domain.Models.Points.WebPoints
import com.google.gson.Gson

class WebPointsSerializer(private val dataJson: String?)
{
    //TODO Change to Generic version
    fun doSerialization(): WebPoints?
    {
        val gson = Gson()
        val webPoints: WebPoints? = gson.fromJson(dataJson, WebPoints::class.java)
        return webPoints
    }
}