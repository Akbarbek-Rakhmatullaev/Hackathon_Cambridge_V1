package com.example.hackathon_cambridge_v1.Domain.Repository

import com.example.hackathon_cambridge_v1.Domain.Models.Point.PointItem
import com.example.hackathon_cambridge_v1.Domain.Models.Points.WebPoints

interface MainInterface
{
    fun getPoints(): WebPoints?

    fun getPoint(id: String): PointItem?
}