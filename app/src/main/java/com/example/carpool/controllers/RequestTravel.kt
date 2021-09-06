package com.example.carpool.controllers

import com.example.carpool.databinding.ActivityRequestTravelBinding

import com.google.android.gms.location.*
import java.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
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
import com.example.carpool.R
import com.example.carpool.RecyclerAdapter.TravelHistory
import com.example.carpool.model.User
import com.google.android.material.navigation.NavigationView

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


    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestTravelBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val userDB: User = intent.getParcelableExtra("userDB")!!
        drawer_header =findViewById(R.id.drawer_user)
        drawer_number = findViewById(R.id.drawer_number)
        drawer_header.text=userDB.name
        drawer_number.text=userDB.phone


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
        val userDB: User = intent.getParcelableExtra("userDB")!!
        when (item.itemId){
            R.id.profile ->{val intent = Intent(this, VerPerfil::class.java)
                intent.putExtra("userDB",userDB)
                startActivity(intent)
            }
            R.id.History ->{val intent = Intent(this, TravelHistory::class.java)
                startActivity(intent)
            }
        }
        drawer = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


}

