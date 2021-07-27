package com.example.carpool


import android.database.CursorIndexOutOfBoundsException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.example.carpool.progressbar.LoadingDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.NumberFormatException

class TravelScreen : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var text: TextView

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

                /*
                text = findViewById(R.id.textViewCoordenadaActual)
                val bundle = intent.extras
                val name= bundle?.getString(COORDENADAS_ACTUALES)
                text.text = "$name"
                text.visibility = View.VISIBLE*/
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
    }

    private fun createMarket() {
        val bundle = intent.extras
        val coordenadaActual= bundle?.getString(COORDENADAS_ACTUALES)
        val coordenadaDestino= bundle?.getString(COORDENADAS_DESTINO)
        try {
            val coordenadas = LatLng(coordenadaActual!!.toDouble(), coordenadaDestino!!.toDouble())
            val market: MarkerOptions = MarkerOptions().position(coordenadas).title("Hola")
            map.addMarker(market)
            // Se agrega una animación cuando muestra la ubicación
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordenadas, 15f),4000, null)
        }catch (e: NumberFormatException){

        }

    }
}

// 19.357652277367926, -99.12299059984987