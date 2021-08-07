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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.NumberFormatException

class TravelScreen : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_screen)

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

    private fun createMarket() {
        val bundle = intent.extras
        val coordenadaActual= bundle?.getString(COORDENADAS_ACTUALES)
        val coordenadaDestino= bundle?.getString(COORDENADAS_DESTINO)
        try {
            val coordenadas = LatLng(coordenadaActual!!.toDouble(), coordenadaDestino!!.toDouble())
            val market: MarkerOptions = MarkerOptions().position(coordenadas).title("Tu destino")
            map.addMarker(market)
            // Se agrega una animación cuando muestra la ubicación
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordenadas, 15f),4000, null)
        }catch (e: NumberFormatException){ }
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
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
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
            Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_LONG).show()
    }
}
