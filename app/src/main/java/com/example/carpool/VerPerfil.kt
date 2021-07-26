package com.example.carpool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class VerPerfil : AppCompatActivity() {

    lateinit var EditNombre:EditText
    lateinit var EditApellido:EditText
    lateinit var EditTelefono:EditText
    lateinit var EditContra:EditText
    lateinit var actbtn:Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_perfil)

        EditApellido=findViewById(R.id.ApellidoEdit)
        EditContra=findViewById(R.id.ContraEdit)
        EditNombre=findViewById(R.id.NombreEdit)
        EditTelefono=findViewById(R.id.NumeroEdit)
        actbtn = findViewById(R.id.Actualizar)

        val bundle=intent.extras
        val usuario=bundle?.get(USER_NAME)
        val pass = bundle?.get(PASSWORD)
        EditNombre.hint=usuario.toString()
        EditContra.hint = pass.toString()
        EditNombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                EditNombre.error = "Usuario no puede estar vacío"


            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (EditNombre.text.isEmpty())
                    EditNombre.error="Usuario no puede esta vacio"
            }
        })
        actbtn.setOnClickListener {
            if(EditApellido.text.isEmpty()||EditContra.text.isEmpty()||EditNombre.text.isEmpty()||EditTelefono.text.isEmpty()){
                Toast.makeText(this, "Ingrese datos validos", Toast.LENGTH_LONG).show()
                fun EditText.clearError() {
                    error = null
            }}
            else{
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()

                }

            }
        }
    }
