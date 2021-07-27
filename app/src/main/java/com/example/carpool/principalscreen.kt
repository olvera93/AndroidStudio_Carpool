package com.example.carpool

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.*

const val COORDENADAS_ACTUALES ="org.example.activity.COORDENADAS_ACTUALES"
const val COORDENADAS_DESTINO ="org.example.activity.COORDENADAS_DESTINO"



class principalscreen : AppCompatActivity() {

    lateinit var inspector:ImageView
    lateinit var button: Button
    lateinit var coordenadaActual: EditText
    lateinit var coordenadaDestino: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principalscreen)

        coordenadaActual=findViewById(R.id.EditCoordenadaActual)
        coordenadaDestino=findViewById(R.id.EditCoordenadaDestino)
        button=findViewById(R.id.button)

        button.setOnClickListener {


            if (coordenadaActual.text.isEmpty()||coordenadaDestino.text.isEmpty()){
                coordenadaActual.error="Coordenadas incorrectas"
                coordenadaDestino.error="Coordenadas incorrectas"
            }else{

                val bundle =Bundle()
                bundle.putString(COORDENADAS_ACTUALES, coordenadaActual.text.toString())
                bundle.putString(COORDENADAS_DESTINO, coordenadaDestino.text.toString())
                val intent = Intent(this, TravelScreen::class.java).apply {
                    putExtras(bundle)
                }
                startActivity(intent)

                }

            }
        }

}

