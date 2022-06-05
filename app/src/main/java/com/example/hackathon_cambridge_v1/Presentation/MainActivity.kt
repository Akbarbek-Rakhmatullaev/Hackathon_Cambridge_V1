package com.example.hackathon_cambridge_v1.Presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.hackathon_cambridge_v1.BuildConfig
import com.example.hackathon_cambridge_v1.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
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
    private lateinit var navController: NavController
    private lateinit var mainLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initialization()
    }

    fun initialization()
    {
        mainViewModel.getPoints()
//        toolbarLayout = findViewById(R.id.main_activity_toolbar_layout)
//        toolbar = findViewById(R.id.main_activity_toolbar)
//        toolbarLayout.visibility = View.GONE
//        mainViewModel.toolbarLiveData.observe(this, Observer {
//            if(it == true)
//                toolbarLayout.visibility = View.VISIBLE
//        })
//        navController = findNavController(R.id.main_activity_fragment_host)
//        mainLayout = findViewById(R.id.main_activity_drawer_layout)
//        navigationView.setupWithNavController(navController)
    }
}