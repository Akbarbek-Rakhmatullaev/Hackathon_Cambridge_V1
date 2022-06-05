package com.example.hackathon_cambridge_v1.Data.Repository

import android.util.Log
import com.example.hackathon_cambridge_v1.Data.Serializers.WebPointSerializer
import com.example.hackathon_cambridge_v1.Data.Serializers.WebPointsSerializer
import com.example.hackathon_cambridge_v1.Data.Web.Rest.WebGetPoint
import com.example.hackathon_cambridge_v1.Data.Web.Rest.WebGetPoints
import com.example.hackathon_cambridge_v1.Domain.Models.Point.PointItem
import com.example.hackathon_cambridge_v1.Domain.Models.Points.PointsItem
import com.example.hackathon_cambridge_v1.Domain.Models.Points.WebPoints
import com.example.hackathon_cambridge_v1.Domain.Repository.MainInterface

class MainInterfaceImpl: MainInterface
{
    override fun getPoints(): WebPoints?
    {
        val getPoints = WebGetPoints()
        val dataJson = getPoints.getPoints()
        val pointsSerializer = WebPointsSerializer(dataJson)
        val webPoints: WebPoints? = pointsSerializer.doSerialization()
        Log.d("MyLog","Web Points: $webPoints")
        return webPoints
    }

    override fun getPoint(id: String): PointItem?
    {
        val getPoint = WebGetPoint(id)
        val dataJson = getPoint.getPoint()
        val pointSerializer = WebPointSerializer(dataJson)
        val pointItem: PointItem? = pointSerializer.doSerialization()
        Log.d("MyLog","Web Point: $pointItem")
        return pointItem
    }
}