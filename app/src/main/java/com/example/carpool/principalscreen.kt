package com.example.carpool

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

const val COORDENADAS_ACTUALES ="org.example.activity.COORDENADAS_ACTUALES"
const val COORDENADAS_DESTINO ="org.example.activity.COORDENADAS_DESTINO"



class principalscreen : AppCompatActivity() {

    lateinit var inspector:ImageView
    lateinit var buttonCoordenadas: Button
    lateinit var coordenadaActual: EditText
    lateinit var coordenadaDestino: EditText
    lateinit var buttonverPerfil:  Button

    //Descarga bundle de Login



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principalscreen)

        val recycler = findViewById<RecyclerView>(R.id.recycler)


        val bundle1=intent.extras
        val usuario=bundle1?.get(USER_NAME)

        coordenadaActual=findViewById(R.id.EditCoordenadaActual)
        coordenadaDestino=findViewById(R.id.EditCoordenadaDestino)
        buttonCoordenadas=findViewById(R.id.button)
        buttonverPerfil=findViewById(R.id.VerPerfilbtn)

        val btnTravelHistory = findViewById<Button>(R.id.btnTravelHistory)

        buttonCoordenadas.setOnClickListener {

            if (coordenadaActual.text.isEmpty()||coordenadaDestino.text.isEmpty()){
                coordenadaActual.error="Coordenadas no debe estar vacío"
                coordenadaDestino.error="Coordenadas no debe estar vacío"
            } else {
                if (coordenadaActual.text.toString().toDouble() <= -90.0 && coordenadaActual.text.toString().toDouble() <= 90.0 && coordenadaDestino.text.toString().toDouble() <= -180.0 && coordenadaDestino.text.toString().toDouble() <= 180.0 ) {
                    coordenadaActual.error="Coordenadas incorrectas"
                    coordenadaDestino.error="Coordenadas incorrectas"

                } else {
                    val bundle2 =Bundle()
                    bundle2.putString(COORDENADAS_ACTUALES, coordenadaActual.text.toString())
                    bundle2.putString(COORDENADAS_DESTINO, coordenadaDestino.text.toString())
                    val intent = Intent(this, TravelScreen::class.java).apply {
                        putExtras(bundle2)
                    }
                    startActivity(intent)
                }
            }




        }
        buttonverPerfil.setOnClickListener {
            val bundle3 =Bundle()
            bundle3.putString(USER_NAME, usuario.toString())
            val intent2 = Intent(this,VerPerfil::class.java).apply {
            putExtras(bundle3)
            }
            startActivity(intent2)
        }

        btnTravelHistory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(USER_NAME, usuario.toString())
            val intent = Intent(this, TravelHistory::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)


        }
    }

}

