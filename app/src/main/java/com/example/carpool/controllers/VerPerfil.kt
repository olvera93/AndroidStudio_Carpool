package com.example.carpool.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.carpool.R
import com.example.carpool.model.User
import com.example.carpool.principalscreen

class VerPerfil : AppCompatActivity() {
    lateinit var EditNombre:EditText
    lateinit var EditUsuario:EditText
    lateinit var EditTelefono:EditText
    lateinit var EditContra:EditText
    lateinit var actbtn:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_perfil)
        val userDB: User = intent.getParcelableExtra("userDB")!!

        EditUsuario=findViewById(R.id.UsuarioEdit)
        EditContra=findViewById(R.id.ContraEdit)
        EditNombre=findViewById(R.id.NombreEdit)
        EditTelefono=findViewById(R.id.NumeroEdit)
        actbtn = findViewById(R.id.Actualizar)

        EditUsuario.setText(userDB.user)
        EditNombre.setText(userDB.name)
        EditContra.setText(userDB.password)
        EditTelefono.setText(userDB.phone)

        EditNombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                EditNombre.error = "Usuario no puede estar vacío"


            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (EditUsuario.text.toString().isEmpty() ||EditContra.text.toString().isEmpty() ||EditNombre.text.toString().isEmpty() ||EditTelefono.text.toString().isEmpty()){
                    EditNombre.error="Nombre no puede estar vacio"
                    EditTelefono.error="Telefono no puede estar vacio"
                    EditContra.error = "Contraseña no puede estar vacia"
                    EditUsuario.error = "Usuario no puede estar vacio"
                }

            }
        })
        actbtn.setOnClickListener {
            if(EditUsuario.text.toString().isEmpty() ||EditContra.text.toString().isEmpty() ||EditNombre.text.toString().isEmpty() ||EditTelefono.text.toString().isEmpty()){
                Toast.makeText(this, "Ingrese datos validos", Toast.LENGTH_LONG).show()
                fun EditText.clearError() {
                    error = null
            }}
            else{
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                userDB.user = EditUsuario.text.toString()
                userDB.name = EditNombre.text.toString()
                userDB.password = EditContra.text.toString()
                userDB.phone = EditTelefono.text.toString()

                val intent = Intent(this, principalscreen::class.java)
                intent.putExtra("userDB",userDB)
                startActivity(intent)
                }

            }
        }
    }
