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
        val password = bundle?.getString(PASSWORD)
        text.text="Bienvenido $name"

        button = findViewById(R.id.button2)

        button.setOnClickListener {
            val bundle =Bundle()
            bundle.putString(USER_NAME, name)
            bundle.putString(PASSWORD,password)


            val intent = Intent(this, VerPerfil::class.java).apply {
                putExtras(bundle)
            }
            startActivity(intent)
        }
        }
    }
