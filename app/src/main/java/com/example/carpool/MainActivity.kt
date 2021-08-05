package com.example.carpool

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carpool.controllers.VerPerfil
import com.example.carpool.model.User


class MainActivity : AppCompatActivity() {

    //Inicio de componentes
    lateinit var usuario: EditText
    lateinit var contrasena:EditText
    lateinit var button: Button


    //Creacion de interfaz
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Descarga de usuarios provinientes de Registros
        val userDB: User = intent.getParcelableExtra("userDB")!!

        //Instancia de componentes
        button = findViewById(R.id.Login)
        usuario = findViewById(R.id.EditUsuario)

        //Generacion de Listener para cambio de texto en el edit de usuario
        usuario.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                usuario.error = "Usuario no puede estar vacío"
                button.isEnabled = s.toString().isNotEmpty()

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        //Instancia de contraseña
        contrasena = findViewById(R.id.EditContrasena)

        //Generacion de Listener para cambio de texto en el edit de contraseña
        contrasena.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                button.isEnabled = s.toString().isNotEmpty()

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })


        //Iniciamos el listener sobre el boton de Login
        button.setOnClickListener {
            if (!validacionCampos(usuario.text.toString(),contrasena.text.toString())) //Validacion de campos
                 {
                Toast.makeText(this, "Usuario o contraseña invalidos", Toast.LENGTH_LONG).show()
                usuario.error = "No debe de estar vacio"
                contrasena.error = "Contraseña incorrecta"

                fun EditText.clearError() {
                    error = null
                }

            } else {
                //Validacion si existe usuario en la base de datos de la clase User
                if(userDB.validateUser(usuario.text.toString(),contrasena.text.toString())){
                    Toast.makeText(this, "Ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, principalscreen::class.java)
                    intent.putExtra("userDB",userDB)

                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Usuario/Contraseña incorrectos :(", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
    //Funcion de validacion de campos
fun validacionCampos(campo1:String,campo2:String):Boolean{
    return campo1.isNotEmpty()&&campo1!=null || campo2.length>0 &&campo2!=null
        }
