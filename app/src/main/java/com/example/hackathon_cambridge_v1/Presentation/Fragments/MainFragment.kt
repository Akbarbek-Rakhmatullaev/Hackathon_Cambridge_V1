package com.example.hackathon_cambridge_v1.Presentation.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hackathon_cambridge_v1.BuildConfig
import com.example.hackathon_cambridge_v1.Presentation.MainViewModel
import com.example.hackathon_cambridge_v1.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MainFragment: Fragment()
{
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var toolbarLayout: AppBarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API)
        MapKitFactory.initialize(activity)
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?) : View?
    {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?)
    {
        super.onViewCreated(view,savedInstanceState)
        initialization(view)
    }

    private fun initialization(view: View)
    {
        toolbarLayout = view.findViewById(R.id.main_fragment_toolbar_layout)
        toolbar = view.findViewById(R.id.main_fragment_toolbar)
        mapView = view.findViewById(R.id.main_fragment_mapView)
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