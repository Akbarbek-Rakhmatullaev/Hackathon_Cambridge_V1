package com.example.hackathon_cambridge_v1.Domain.UseCases

import com.example.hackathon_cambridge_v1.Domain.Models.Point.PointItem
import com.example.hackathon_cambridge_v1.Domain.Repository.MainInterface

class GetPointUseCase(private val mainInterface: MainInterface)
{
    fun execute(id: String): PointItem?
    {
        return mainInterface.getPoint(id)
    }
}