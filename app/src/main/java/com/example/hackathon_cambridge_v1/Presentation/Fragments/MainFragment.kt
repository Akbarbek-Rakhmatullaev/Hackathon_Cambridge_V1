package com.example.hackathon_cambridge_v1.Presentation.Fragments

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Align
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hackathon_cambridge_v1.BuildConfig
import com.example.hackathon_cambridge_v1.Domain.Models.Point.ChildsItem
import com.example.hackathon_cambridge_v1.Domain.Models.Point.FuelCategoryItem
import com.example.hackathon_cambridge_v1.Domain.Models.Points.WebPoints
import com.example.hackathon_cambridge_v1.Presentation.Adapters.PointCategoriesAdapter
import com.example.hackathon_cambridge_v1.Presentation.MainViewModel
import com.example.hackathon_cambridge_v1.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.*
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.*
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random


class MainFragment: Fragment(), UserLocationObjectListener, DrivingSession.DrivingRouteListener,
        GeoObjectTapListener, InputListener, Session.SearchListener, ClusterListener, ClusterTapListener,
        MapObjectTapListener
{
    //base view and vars
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var toolbarLayout: AppBarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var positionButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var checkerLayout: RelativeLayout
    private lateinit var sheetHolder: CoordinatorLayout
    private lateinit var sheet: RelativeLayout
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

    //routing, searching and navigation views and vars
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingSession: DrivingSession

    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    //adding clustered points
    private val MARKS_NUMBER: Int = 50
    private lateinit var clusters: List<Point>

    //point views
    private lateinit var pointName: TextView
    private lateinit var pointAddress: TextView
    private lateinit var pointCompanyName: TextView
    private lateinit var pointWorking: TextView
    private lateinit var expandableListView: ExpandableListView
    private lateinit var expandableListViewAdapter: ExpandableListAdapter
    private lateinit var pointViewAdapter: PointCategoriesAdapter
    private var categoryTitles: ArrayList<FuelCategoryItem?> = ArrayList()
    private var categoryDetails: HashMap<String, ArrayList<ChildsItem>?> = HashMap()
    //adding points
    private lateinit var mapObjectTapListener: MapObjectTapListener

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API)
        MapKitFactory.initialize(activity)
        DirectionsFactory.initialize(activity)
        SearchFactory.initialize(activity)
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
        //base view and vars
        positionButton = view.findViewById(R.id.main_fragment_position_button)
        //searchEditText = view.findViewById(R.id.fragment_main_search_editText)
//        toolbarLayout = view.findViewById(R.id.main_fragment_toolbar_layout)
//        toolbar = view.findViewById(R.id.main_fragment_toolbar)
//        toolbarLayout.visibility = View.GONE
        checkerLayout = view.findViewById(R.id.main_fragment_checker_layout)
        sheetHolder = view.findViewById(R.id.main_fragment_sheet_holder)
        sheet = view.findViewById(R.id.main_fragment_sheet)
        BottomSheetBehavior.from(sheet).apply {
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }
        sheetHolder.visibility = View.GONE
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
                //toolbarLayout.visibility = View.VISIBLE
                mainViewModel.getCameraPosition(userLocationLayer)
                Log.d("MyLog","Once Updated")
            }

            override fun onLocationStatusUpdated(p0: LocationStatus)
            {
                Log.d("MyLog","Once Status Updated")
            }

        }
        locationManager.requestSingleUpdate(locationListener)

        Log.d("MyLog","MAP KIT ENABLED")

        //routing, searching and navigation views and vars
        //-ROUTING
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = map.mapObjects.addCollection()
        submitRequest()
        //-CLICKING

        map.addTapListener(this)
        map.addInputListener(this)
        //-SEARCHING
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
//        searchEditText.setOnEditorActionListener(object: TextView.OnEditorActionListener
//        {
//            override fun onEditorAction(v: TextView?,actionId: Int,event: KeyEvent?): Boolean
//            {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH)
//                {
//                    //submitQuery(searchEditText.text.toString())
//                    Log.d("MyLog","SEARCH IS SENT")
//                }
//                return false
//            }
//
//        })
        //submitQuery("Novza")
        //adding clustered points
//        val imageProvider = ImageProvider.fromResource(activity, R.drawable.ic_search_result)
//        val clusterizedCollection: ClusterizedPlacemarkCollection = map.mapObjects.addClusterizedPlacemarkCollection(this)
//        val points: List<Point> = createPoints(clusters.size)
//        clusterizedCollection.addPlacemarks(points, imageProvider, IconStyle())
//        clusterizedCollection.clusterPlacemarks(60.0, 12)

        //adding points
        mapObjectTapListener = MapObjectTapListener { mapObject, point ->
            Log.d("MyLog","Map Object: ${mapObject.userData}")
            BottomSheetBehavior.from(sheet).apply {
                peekHeight = 300
                this.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            sheetHolder.visibility = View.VISIBLE
            mainViewModel.getPoint(mapObject.userData as String)
            true }

        mainViewModel.pointsLiveData.observe(viewLifecycleOwner, Observer {
            val webPoints: WebPoints? = it
            if(webPoints != null)
            {
                val pointsSize: Int? = webPoints.count
                for (i in 0 until pointsSize!!)
                {
                    val latitude = webPoints.items?.get(i)?.coords?.latitude
                    val longitude = webPoints.items?.get(i)?.coords?.longitude
                    val point: Point = Point(latitude!!, longitude!!)
                    val placemark: PlacemarkMapObject = mapObjects.addPlacemark(point,
                        ImageProvider.fromBitmap(drawBitMap(requireContext())))
                    placemark.apply {
                        userData = webPoints.items[i]?.id
                        addTapListener(mapObjectTapListener)
                    }
                }
            }
        })
        pointName = view.findViewById(R.id.main_fragment_point_name_textView)
        pointAddress = view.findViewById(R.id.main_fragment_point_address_textView)
        pointCompanyName = view.findViewById(R.id.main_fragment_point_company_name_textView)
        pointWorking = view.findViewById(R.id.main_fragment_point_working_textView)

        expandableListView = view.findViewById(R.id.main_fragment_point_categories_listView)

        mainViewModel.pointLiveData.observe(viewLifecycleOwner, Observer {
            if(it != null)
            {
                pointName.text = it.name
                pointAddress.text = it.address
                pointCompanyName.text = it.companyName
                pointWorking.text = "Is Working: " + it.isWorking.toString()
                if(it.fuelSelection != null)
                {
                    for (i in 0 until it.fuelSelection.size)
                    {
                        categoryTitles.add(it.fuelSelection[i]!!)
                        val details: ArrayList<ChildsItem> = ArrayList()
                        for (j in 0 until it.fuelSelection[i]?.childs?.size!!)
                        {
                            details.add(it.fuelSelection[i]?.childs!![j]!!)
                        }
                        categoryDetails.put(it.fuelSelection[i]?.id.toString(), details)
                    }
                }

                pointViewAdapter = PointCategoriesAdapter(requireContext(), categoryTitles, categoryDetails)

                expandableListView.setAdapter(pointViewAdapter)
            }
        })
    }

    //base view and vars
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

    //routing, searching and navigation views and vars
    //-ROUTING
    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>)
    {
        for(route in p0)
        {
            mapObjects.addPolyline(route.geometry)
        }
    }

    override fun onDrivingRoutesError(p0: Error)
    {
        var errorMessage: String = getString(R.string.unknown_error_message)
        if(p0 is RemoteError)
        {
            errorMessage = getString(R.string.remote_error_message)
        }
        else if (p0 is NetworkError)
        {
            errorMessage = getString(R.string.network_error_message)
        }
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun submitRequest()
    {
        mainViewModel.cameraPositionLiveData.observe(viewLifecycleOwner, Observer {
            val cameraPosition = it
            if(cameraPosition != null)
            {
                val routeStartPoint: Point = Point(cameraPosition.target.latitude, cameraPosition.target.longitude)
                val routeEndPoint: Point = Point(41.7, 69.8)
                val drivingOptions = DrivingOptions()
                val vehicleOptions = VehicleOptions()
                val requestPoints: ArrayList <RequestPoint> = ArrayList()
                requestPoints.add(RequestPoint(routeStartPoint, RequestPointType.WAYPOINT, null))
                requestPoints.add(RequestPoint(routeEndPoint, RequestPointType.WAYPOINT, null))
                drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
                Log.d("MyLog","ROUTE BUILT")
            }
            else
                Toast.makeText(activity, "Still Loading", Toast.LENGTH_SHORT).show()
        })
    }

    //- CLICKING AND SEARCHING
    override fun onObjectTap(p0: GeoObjectTapEvent): Boolean
    {
        val selectionMetadata: GeoObjectSelectionMetadata = p0.geoObject.metadataContainer.
        getItem(GeoObjectSelectionMetadata::class.java)
        mapView.map.selectGeoObject(selectionMetadata.id, selectionMetadata.layerId)
        Log.d("MyLog","Tapped Location Id: ${selectionMetadata.id}\n" +
                "Tapped Location Layer Id: ${selectionMetadata.id}")
        return true
    }

    override fun onMapTap(p0: Map,p1: Point)
    {
        mapView.map.deselectGeoObject()
        sheetHolder.visibility = View.GONE
    }

    override fun onMapLongTap(p0: Map,p1: Point)
    {

    }

    override fun onSearchResponse(p0: Response)
    {
        mapObjects.clear()
        for(searchResult in p0.collection.children)
        {
            val resultPoint: Point? = searchResult.obj?.geometry?.get(0)?.point
            if(resultPoint != null)
            {
                mapObjects.addPlacemark(resultPoint, ImageProvider.fromResource(activity, R.drawable.ic_search_result))
            }
        }
    }

    override fun onSearchError(p0: Error)
    {
        var errorMessage: String = getString(R.string.unknown_error_message)
        if(p0 is RemoteError)
        {
            errorMessage = getString(R.string.remote_error_message)
        }
        else if(p0 is NetworkError)
        {
            errorMessage = getString(R.string.network_error_message)
        }
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun submitQuery(query: String)
    {
        searchSession = searchManager.submit(query, VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
        SearchOptions(), this)
    }


    //adding clustered points
    override fun onClusterAdded(p0: Cluster)
    {
        Log.d("MyLog","Cluster Added")
        p0.appearance.setIcon(TextImageProvider((p0.size).toString(), requireContext()))
        p0.addClusterTapListener(this)
    }


    override fun onClusterTap(p0: Cluster): Boolean
    {
        Toast.makeText(
            activity,
            "Cluster Tapped + Cluster Size: ${p0.size}",Toast.LENGTH_SHORT).show()
        return true
    }

    private fun createPoints(count: Int): List<Point>
    {
        val points: ArrayList<Point> = ArrayList()
        val random = Random
        for(i in 0 until count)
        {
            val clusterCenter: Point = clusters[random.nextInt(clusters.size)]
            val latitude: Double = clusterCenter.latitude
            val longitude: Double = clusterCenter.longitude

            points.add(Point(latitude, longitude))
        }
        return points
    }

    class TextImageProvider(private val text: String, private val context: Context): ImageProvider()
    {
        override fun getId(): String
        {
            return "text_$text"
        }

        override fun getImage(): Bitmap
        {
            val FONT_SIZE: Float = 18f
            val MARGIN_SIZE: Float = 12f
            val STROKE_SIZE: Float = 4f
            val metrics = DisplayMetrics()
            val manager: WindowManager? = context.getSystemService(WindowManager::class.java)
            manager?.defaultDisplay?.getMetrics(metrics)

            val textPaint = Paint()
            textPaint.textSize = FONT_SIZE * metrics.density
            textPaint.textAlign = Align.CENTER
            textPaint.style = Paint.Style.FILL
            textPaint.isAntiAlias = true

            val widthF: Float = textPaint.measureText(text)
            val textMetrics: Paint.FontMetrics = textPaint.fontMetrics
            val heightF: Float = abs(textMetrics.bottom) + abs(textMetrics.top)
            val textRadius = sqrt((widthF*widthF + heightF*heightF).toDouble()).toFloat()/2
            val internalRadius: Float = textRadius + MARGIN_SIZE*metrics.density
            val externalRadius: Float = internalRadius + STROKE_SIZE*metrics.density

            val width = (2*externalRadius + 0.5).toInt()

            val bitmap = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val backgroundPaint = Paint()
            backgroundPaint.isAntiAlias = true
            backgroundPaint.color = context.getColor(R.color.primary_green)
            canvas.drawCircle((width/2).toFloat(),(width/2).toFloat(),externalRadius,backgroundPaint)

            backgroundPaint.color = Color.WHITE
            canvas.drawCircle((width/2).toFloat(),(width/2).toFloat(),internalRadius,backgroundPaint)

            canvas.drawText(text,(width/2).toFloat(),
                width/2 - (textMetrics.ascent + textMetrics.descent)/2,
                textPaint)

            return bitmap
        }
    }

    private fun drawBitMap(context: Context): Bitmap
    {

        val source: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_location)
        val bitmap: Bitmap = source.copy(Bitmap.Config.ARGB_8888, true)
        val paint = Paint()
        val filter: ColorFilter = PorterDuffColorFilter(context.getColor(R.color.primary_green),
            PorterDuff.Mode.SRC_IN)
        paint.colorFilter = filter
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f , 0f, paint)
        return bitmap
    }

    override fun onMapObjectTap(p0: MapObject,p1: Point): Boolean
    {
        Toast.makeText(
            activity,
            "Point Tapped",Toast.LENGTH_SHORT).show()
        return true
    }
}