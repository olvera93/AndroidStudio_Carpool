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
    //TO-DO enlazar los datos de el usuario para que aparezcan como valor default en editTexts
    lateinit var EditNombre:EditText
    lateinit var EditUsuario:EditText
    lateinit var EditTelefono:EditText
    lateinit var EditContra:EditText
    lateinit var actbtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_perfil)

        EditUsuario=findViewById(R.id.UsuarioEdit)
        EditContra=findViewById(R.id.ContraEdit)
        EditNombre=findViewById(R.id.NombreEdit)
        EditTelefono=findViewById(R.id.NumeroEdit)
        actbtn = findViewById(R.id.Actualizar)

        val userDB: User = intent.getParcelableExtra("userDB")!!

        EditUsuario.hint=userDB.user.toString()
        EditNombre.hint=userDB.name.toString()
        EditContra.hint=userDB.password.toString()
        EditTelefono.hint=userDB.phone.toString()
        //EditContra.hint = pass.toString()
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
            if(EditUsuario.text.isEmpty()||EditContra.text.isEmpty()||EditNombre.text.isEmpty()||EditTelefono.text.isEmpty()){
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
