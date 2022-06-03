package com.example.hackathon_cambridge_v1.Presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathon_cambridge_v1.BuildConfig
import com.example.hackathon_cambridge_v1.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class MainActivity : AppCompatActivity()
{
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var toolbarLayout: AppBarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API)
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_main)
        initialization()
    }

    fun initialization()
    {

        mapView = findViewById(R.id.main_fragment_mapView)
        mapView.map.move(
            CameraPosition(Point(5.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f), null)
    }

    override fun onStop()
    {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart()
    {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

}