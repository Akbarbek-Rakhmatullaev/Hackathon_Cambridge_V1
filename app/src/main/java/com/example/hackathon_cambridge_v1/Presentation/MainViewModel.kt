package com.example.hackathon_cambridge_v1.Presentation

import android.app.Application
import androidx.lifecycle.*
import com.example.hackathon_cambridge_v1.Data.Repository.MainInterfaceImpl
import com.example.hackathon_cambridge_v1.Domain.Models.Point.PointItem
import com.example.hackathon_cambridge_v1.Domain.Models.Points.WebPoints
import com.example.hackathon_cambridge_v1.Domain.UseCases.GetPointUseCase
import com.example.hackathon_cambridge_v1.Domain.UseCases.GetPointsUseCase
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application)
{
    private val cameraPositionData: MutableLiveData<CameraPosition?> = MutableLiveData<CameraPosition?>()
    val cameraPositionLiveData: LiveData<CameraPosition?> = cameraPositionData

    private val toolbarData = MutableLiveData<Boolean>()
    val toolbarLiveData: LiveData<Boolean> = toolbarData

    private val pointsData = MutableLiveData<WebPoints?>()
    val pointsLiveData: LiveData<WebPoints?> = pointsData

    private val pointData = MutableLiveData<PointItem?>()
    val pointLiveData: LiveData<PointItem?> = pointData

    private val mainInterfaceImpl = MainInterfaceImpl()

    fun getCameraPosition(userLocationLayer: UserLocationLayer)
    {
        val cameraPosition = userLocationLayer.cameraPosition()
        cameraPositionData.value = cameraPosition
    }

    fun singleUpdate(mapKit: MapKit, mapView: MapView, currentLocation: Location?)
    {
    }

    fun changeToolbarVisibility(state: Boolean)
    {
        toolbarData.value = state
    }

    fun getPoints()
    {
        viewModelScope.launch(Dispatchers.IO) {
            val getPointsUseCase = GetPointsUseCase(mainInterfaceImpl)
            pointsData.postValue(getPointsUseCase.execute())
        }
    }

    fun getPoint(id: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val getPointUseCase = GetPointUseCase(mainInterfaceImpl)
            pointData.postValue(getPointUseCase.execute(id))
        }
    }
}