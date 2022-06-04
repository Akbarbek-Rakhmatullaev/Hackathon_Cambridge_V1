package com.example.hackathon_cambridge_v1.Presentation.Fragments

import android.Manifest
import android.graphics.Color
import android.graphics.PointF
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.hackathon_cambridge_v1.BuildConfig
import com.example.hackathon_cambridge_v1.Presentation.MainViewModel
import com.example.hackathon_cambridge_v1.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.*
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch


class MainFragment: Fragment(), UserLocationObjectListener
{
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var toolbarLayout: AppBarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var positionButton: Button
    private lateinit var checkerLayout: RelativeLayout
    private lateinit var mapView: MapView
    private lateinit var mapKit: MapKit
    private var mapKitEnabled: Boolean = false
    private lateinit var userLocationLayer: UserLocationLayer
    private val PERMISSIONS_REQUEST_FINE_LOCATION: Int = 1
    private val targetLatitude: Double = 41.311081
    private val targetLongitude: Double = 69.240562
    private var currentLocation: Location? = null
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API)
        MapKitFactory.initialize(activity)
        Log.d("MyLog","MAP KIT FACTORY")
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
        positionButton = view.findViewById(R.id.main_fragment_position_button)
        toolbarLayout = view.findViewById(R.id.main_fragment_toolbar_layout)
        toolbar = view.findViewById(R.id.main_fragment_toolbar)
        checkerLayout = view.findViewById(R.id.main_fragment_checker_layout)
        mapView = view.findViewById(R.id.main_fragment_mapView)
        val map = mapView.map
        map.isRotateGesturesEnabled = false
        map.move(
            CameraPosition(Point(targetLatitude, targetLongitude), 14.0f, 0.0f, 0.0f))

        positionButton.setOnClickListener {
            if (mapKitEnabled == true)
            {
                val cameraPosition = userLocationLayer.cameraPosition()

                if (cameraPosition != null)
                {
                    map.move(CameraPosition(cameraPosition.target, cameraPosition.zoom,0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH,0.5f),null)
                }
                else
                    Toast.makeText(activity, "Still Loading", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(activity, "Still Loading", Toast.LENGTH_SHORT).show()
            }
        }
        mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)

        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)

        locationManager = mapKit.createLocationManager()
        locationListener = object : LocationListener
        {
            override fun onLocationUpdated(p0: Location)
            {
                map.move(CameraPosition(Point(p0.position.latitude,
                    p0.position.longitude),16.0f,0.0f,0.0f),
                    Animation(Animation.Type.SMOOTH,0f),
                    null)
                mapKitEnabled = true
                checkerLayout.visibility = View.GONE
                Log.d("MyLog","Once Updated")
            }

            override fun onLocationStatusUpdated(p0: LocationStatus)
            {
                Log.d("MyLog","Once Status Updated")
            }

        }
        locationManager.requestSingleUpdate(locationListener)

        Log.d("MyLog","MAP KIT ENABLED")
        Log.d("MyLog","CAMERA ")
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

    override fun onObjectAdded(p0: UserLocationView)
    {
//        userLocationLayer.setAnchor(
//            PointF(((mapView.width() *0.5).toFloat()), ((mapView.height() * 0.5).toFloat())),
//            PointF(((mapView.width() *0.5).toFloat()), ((mapView.height() * 0.5).toFloat())))

        p0.arrow.setIcon(ImageProvider.fromResource(activity, R.drawable.ic_navigation))

        val pinIcon: CompositeIcon = p0.pin.useCompositeIcon()

        pinIcon.setIcon("icon", ImageProvider.fromResource(activity, R.drawable.ic_icon),
            IconStyle().setAnchor(PointF(0f, 0f)).setRotationType(RotationType.ROTATE).setZIndex(0f).setScale(1f))

        pinIcon.setIcon("pin", ImageProvider.fromResource(activity, R.drawable.ic_search_result),
            IconStyle().setAnchor(PointF(0.5f, 0.5f)).setRotationType(RotationType.ROTATE).setZIndex(1f).setScale(0.5f))

        p0.accuracyCircle.fillColor = Color.GREEN and 0x99ffffff.toInt()
    }

    override fun onObjectRemoved(p0: UserLocationView)
    {}

    override fun onObjectUpdated(p0: UserLocationView,p1: ObjectEvent)
    {}

}