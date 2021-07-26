package com.example.carpool

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


class principalscreen : AppCompatActivity() {

    lateinit var inspector:ImageView
    lateinit var button: Button
    lateinit var textView: TextView
    lateinit var CoordenadaActual: EditText
    lateinit var CoordenadaDestino: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principalscreen)

        CoordenadaActual=findViewById(R.id.EditCoordenadaActual)
        CoordenadaDestino=findViewById(R.id.EditCoordenadaDestino)
        button=findViewById(R.id.button)
        textView = findViewById(R.id.textView)

        button.setOnClickListener {
            if (CoordenadaActual.text.isEmpty()||CoordenadaDestino.text.isEmpty()){
                CoordenadaActual.setText(R.string.ErrorCoordenada)
                CoordenadaDestino.setText(R.string.ErrorCoordenada)
                CoordenadaActual.error="Coordenadas incorrectas"
                CoordenadaDestino.error="Coordenadas incorrectas"
            }else{

                CoordenadaActual.setText(R.string.OKCoordenada)
                CoordenadaDestino.setText(R.string.OKCoordenada)
                }

            }
        }

    }

}