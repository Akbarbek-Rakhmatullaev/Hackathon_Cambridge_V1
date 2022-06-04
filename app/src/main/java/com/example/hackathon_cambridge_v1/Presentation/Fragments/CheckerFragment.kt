package com.example.hackathon_cambridge_v1.Presentation.Fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hackathon_cambridge_v1.BuildConfig
import com.example.hackathon_cambridge_v1.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.launch

class CheckerFragment: Fragment()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?) : View?
    {
        return inflater.inflate(R.layout.fragment_checker, container, false)
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?)
    {
        super.onViewCreated(view,savedInstanceState)
        initialization(view)
        requestLocation()
    }

    private fun initialization(view: View)
    {

    }

    private fun requestLocation()
    {
//        val permissionString: String = "android.permission.ACCESS_FINE_LOCATION"
//        if(ContextCompat.checkSelfPermission(requireContext(), permissionString)
//            != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>(permissionString),
//                PERMISSIONS_REQUEST_FINE_LOCATION)
//        }

        val permissionlistener: PermissionListener = object : PermissionListener
        {
            override fun onPermissionGranted()
            {
                Toast.makeText(activity,"Permission Granted",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_checkerFragment_to_mainFragment)
            }

            override fun onPermissionDenied(deniedPermissions: List<String>)
            {
                Toast.makeText(activity,"Permission Denied\n$deniedPermissions",Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val permissionListener = TedPermission.create().setPermissionListener(permissionlistener).setDeniedMessage("Denied").
        setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).check()
    }
}