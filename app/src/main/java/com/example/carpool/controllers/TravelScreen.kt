package com.example.carpool.controllers


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.carpool.COORDENADAS_ACTUALES
import com.example.carpool.COORDENADAS_DESTINO
import com.example.carpool.R
import com.example.carpool.progressbar.LoadingDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.NumberFormatException
import android.graphics.Color
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.lang.Exception


class TravelScreen : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_screen)

        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val loading = LoadingDialog(this)
        loading.startLoadingDialog()


        val handler = Handler()
        handler.postDelayed(object: Runnable {
            override fun run() {
                loading.dismiss()
                createFragment()
            }
        }, 5000)



    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarket()
        enableLocation()

    }

    @SuppressLint("MissingPermission")
    private fun createMarket() {
        val bundle = intent.extras
        val coordenadaActual= bundle?.getString(COORDENADAS_ACTUALES)
        val coordenadaDestino= bundle?.getString(COORDENADAS_DESTINO)
        try {


            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->


                    // Se coloca
                    val latLngOrigin = LatLng(location!!.latitude.toDouble(), location!!.longitude.toDouble())
                    val latLngDestination = LatLng(coordenadaActual!!.toDouble(), coordenadaDestino!!.toDouble())


                    this.map!!.addMarker(MarkerOptions().position(latLngOrigin).title(getString(R.string.current_location)))
                    this.map!!.addMarker(MarkerOptions().position(latLngDestination).title(getString(R.string.destination_location)))
                    this.map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))

                    val path: MutableList<List<LatLng>> = ArrayList()
                    val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=${latLngOrigin.latitude},${latLngOrigin.longitude}&destination=${latLngDestination.latitude},${latLngDestination.longitude}&mode=driving&key=AIzaSyDdb8SoncgLZzAraV3h6rOGIXwV-PB66gQ"
                    val directionsRequest = object : StringRequest(Request.Method.GET, urlDirections, Response.Listener<String> {
                            response ->
                        val jsonResponse = JSONObject(response)
                        // Obtener rutas

                        val routes = jsonResponse.getJSONArray("routes")
                        if (routes.length() > 0) {
                            val legs = routes.getJSONObject(0).getJSONArray("legs")
                            val steps = legs.getJSONObject(0).getJSONArray("steps")
                            for (i in 0 until steps.length()) {
                                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                                path.add(PolyUtil.decode(points))
                            }
                            for (i in 0 until path.size) {
                                this.map!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                            }
                        } else {
                            Toast.makeText(this,getString(R.string.invalid_coordinate), Toast.LENGTH_LONG).show()
                        }

                    }, Response.ErrorListener {
                            _ ->
                    }){}
                    val requestQueue = Volley.newRequestQueue(this)
                    requestQueue.add(directionsRequest)
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.not_get_location),
                        Toast.LENGTH_SHORT).show()
                }
        }catch (e: Exception){ }
    }

    //
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    // Va activar la localizacion
    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            //Si dio permiso para obtener su ubicación
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    // Para que el usuario acepte los permisos
    private fun requestLocationPermission() {
        // Ya se le habia pedido al usuario dar permiso pero los rechazo
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, getString(R.string.settings_accept), Toast.LENGTH_LONG).show()
        } else {
            // Es la primera vez que se le pide permisos al usuario
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Para comprobar que el permiso sea aceptado
        when(requestCode) {
            REQUEST_CODE_LOCATION -> {
                // Significa que ha aceptado el permiso
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    map.isMyLocationEnabled = true
                } else {
                    Toast.makeText(this, getString(R.string.activate_location), Toast.LENGTH_LONG).show()
                }

            }
            else -> {}
        }
    }

    /*
    Este metodo sirve por si el usuario sale de la aplicación
    y regresa la localizacion este activa.
    Y si no esta activa para que no crashe la aplicacion
     */
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        @SuppressLint("MissingPermission")
        if (!isLocationPermissionGranted()) { //Si no están permitidos
            map.isMyLocationEnabled = false
        } else {
            Toast.makeText(this, getString(R.string.activate_location), Toast.LENGTH_LONG).show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_LONG).show()
    }
}
