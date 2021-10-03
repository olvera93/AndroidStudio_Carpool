package com.example.carpool.controllers

import com.example.carpool.databinding.ActivityRequestTravelBinding

import com.google.android.gms.location.*
import java.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.carpool.MainActivity
import com.example.carpool.R
import com.example.carpool.RecyclerAdapter.TravelHistory
import com.example.carpool.model.User
import com.example.carpool.model.UserDb
import com.example.carpool.model.Userdbclass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RequestTravel: AppCompatActivity(), OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityRequestTravelBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var slidingUpPanelLayout: SlidingPaneLayout? = null
    private lateinit var txt_welcome: TextView
    var autocompleteFragment: AutocompleteSupportFragment? = null

    private lateinit var drawer: DrawerLayout

    //Drawer header
    lateinit var  drawer_header:TextView
    lateinit var drawer_number:TextView


    //Inicio de sharedPreferences
    lateinit var preferences: SharedPreferences

    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestTravelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE) //SE OBTIENEN LOS SHARED PREFERENCES DE MODO PRIVADO

        notification()

        //App Bar
        val appBar = findViewById<Toolbar>(R.id.app_bar)
        this.setSupportActionBar(appBar)
        setupDrawer(appBar)

        // Obtenga SupportMapFragment y reciba una notificación cuando el mapa esté listo para usarse.
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        init()
        initViews(binding.root)
    }


    private fun initViews(root: View?) {
        slidingUpPanelLayout = findViewById(R.id.activity_travelRequest)
        txt_welcome = findViewById<TextView>(R.id.txt_welcome)

    }

    @SuppressLint("MissingPermission")
    private fun init(){

        // Se inicializa los lugares
        Places.initialize(applicationContext, getString(R.string.google_maps_key))



        // Cree una nueva instancia de cliente de Places.
        val placesClient = Places.createClient(this)

        // Inicialice AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autoComplete_fragment)
                    as AutocompleteSupportFragment

        // Especifica los tipos de datos de lugares que deseas devolver.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Configurar un PlaceSelectionListener para manejar la respuesta.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i(ContentValues.TAG, "Lugarres: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                Log.i(ContentValues.TAG, "Ocurrio un error: $status")
            }
        })
        locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setFastestInterval(3000)
        locationRequest.interval = 5000
        locationRequest.setSmallestDisplacement(10f)


        locationCallback = object: LocationCallback(){
            //Obtenemos nuestra ubicación actual
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                setRestrictPlacesInCountry(locationResult!!.lastLocation)
                val newPos = LatLng(locationResult!!.lastLocation.latitude, locationResult!!.lastLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos, 18f))

            }

        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext!!)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun setRestrictPlacesInCountry(location: Location?) { //Restringir lugares que no son de México
        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            var addresList = geoCoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
            if (addresList.size >0) {
                autocompleteFragment?.setCountry(addresList[0].countryCode)
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        // Para pedir permiso al usuario para activar su ubicación
        Dexter.withContext(applicationContext!!)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true
                    mMap.setOnMyLocationClickListener {

                        fusedLocationProviderClient.lastLocation
                            .addOnFailureListener { e ->
                                Toast.makeText(applicationContext!!, e.message, Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener { location ->
                                val userLatLng = LatLng(location.latitude, location.longitude)
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f))

                            }
                        true
                    }

                    val locationButton = (mapFragment.requireView()!!
                        .findViewById<View>("1".toInt())!!
                        .parent!! as View).findViewById<View>("2".toInt())
                    val params = locationButton.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.ALIGN_TOP, 0)
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                    params.bottomMargin = 50

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(applicationContext!!, "Permission "+response!!.permissionName+ " was denied", Toast.LENGTH_SHORT).show()
                }


            }).check()

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    //Funciones appBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        drawer_header =findViewById(R.id.drawer_user)
        setValues()
        dbOperation()
        drawer_number = findViewById(R.id.drawer_number)


        return super.onCreateOptionsMenu(menu)
    }



    private fun setupDrawer(toolbar: Toolbar){

        drawer = findViewById(R.id.drawer_layout)
        val toggle: ActionBarDrawerToggle = object:
            ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer){
            override fun onDrawerClosed(view: View){
                super.onDrawerClosed(view)

                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)

                //toast("Drawer opened")
            }
        }

        drawer.addDrawerListener(toggle)
        val navigationView:NavigationView= findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener (this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.profile ->{val intent = Intent(this, VerPerfil::class.java)
              
                startActivity(intent)
            }
            R.id.History ->{val intent = Intent(this, TravelHistory::class.java)
                startActivity(intent)
            }
            R.id.LogOut ->{resetShared()
                goToLogged()}
        }
        drawer = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun setValues(){
        //obtenemos los valores desde preferencias
        val user = preferences.getString(MainActivity.USERP,"")

        drawer_header.text = user
    }

    fun resetShared(){
        preferences.edit().clear().commit() //a diferencia de apply, este cambio se hace de forma asíncrona
    }

    private fun dbOperation() {
        //Validacion si existe usuario en la base de datos de la clase User
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            val userArray = UserDb
                .getInstance(this)
                ?.userDao()
                ?.checkRegister(drawer_header.text.toString()) as MutableList<Userdbclass>
            setValuesDrawer(userArray)
        })
    }

    private fun setValuesDrawer(userArray: MutableList<Userdbclass>) {
        runOnUiThread{
        for (user in userArray) {
            drawer_header.text = user.User
            drawer_number.text = user.Phone}

        }
    }

    private fun notification() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Error", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            Log.d("FCM_TOKEN",token)
        })




    }

    fun goToLogged(){
        //cambiar de actividad sin poder regresar a esta con back button
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}