package com.example.hackathon_cambridge_v1.Presentation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.hackathon_cambridge_v1.BuildConfig
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application)
{
    private val permissionData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val permissionLiveData: LiveData<Boolean> = permissionData
    fun initialization(permissionEnabled: Boolean)
    {
        permissionData.value = permissionEnabled
    }

    fun singleUpdate(mapKit: MapKit, mapView: MapView, currentLocation: Location?)
    {
    }

    fun subscribeUpdates()
    {

    }
}