package com.example.hackathon_cambridge_v1.Domain.UseCases

import com.example.hackathon_cambridge_v1.Domain.Models.Points.WebPoints
import com.example.hackathon_cambridge_v1.Domain.Repository.MainInterface

class GetPointsUseCase(private val mainInterface: MainInterface)
{
    fun execute(): WebPoints?
    {
        return mainInterface.getPoints()
    }
}