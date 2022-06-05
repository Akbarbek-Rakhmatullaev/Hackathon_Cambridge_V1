package com.example.hackathon_cambridge_v1.Data.Serializers

import com.example.hackathon_cambridge_v1.Domain.Models.Point.PointItem
import com.example.hackathon_cambridge_v1.Domain.Models.Points.PointsItem
import com.google.gson.Gson

class WebPointSerializer(private val dataJson: String?)
{
    //TODO Change to Generic version
    fun doSerialization(): PointItem?
    {
        val gson = Gson()
        val pointItem: PointItem? = gson.fromJson(dataJson, PointItem::class.java)
        return pointItem
    }
}
