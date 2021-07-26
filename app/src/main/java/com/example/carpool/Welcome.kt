package com.example.carpool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Welcome : AppCompatActivity() {
    private lateinit var text:TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        text=findViewById(R.id.textView2)
        val bundle = intent.extras
        val name= bundle?.getString(USER_NAME)
        text.text="Bienvenido $name"

        button = findViewById(R.id.button2)

        button.setOnClickListener {
            val intent = Intent(this, VerPerfil::class.java).apply {

            }
            startActivity(intent)
        }
        }
    }
