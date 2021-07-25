package com.example.carpool

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


const val USER_NAME ="org.bedu.activity.USER_NAME"

class MainActivity : AppCompatActivity() {

    lateinit var usuario: EditText
    lateinit var contrasena:EditText
    lateinit var inspector:ImageView
    lateinit var button: Button
    lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {


        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button=findViewById(R.id.Login)

        usuario= findViewById(R.id.EditUsuario)
        usuario.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                usuario.error = "Usuario no puede estar vacío"
                button.isEnabled = s.toString().isNotEmpty()

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        contrasena=findViewById(R.id.EditContrasena)
        contrasena.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                button.isEnabled = s.toString().isNotEmpty()

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })


        //Iniciamos el listener
        button.setOnClickListener {
            if (usuario.text.isEmpty() || contrasena.text.isEmpty()){
                Toast.makeText(this, "Usuario o contraseña invalidos", Toast.LENGTH_LONG).show()
                contrasena.error="Contraseña incorrecta"
                
                fun EditText.clearError() {
                    error = null
                }

            }else{
                Toast.makeText(this, "Ha iniciado sesión correctamente", Toast.LENGTH_LONG).show()
                val bundle =Bundle()
                bundle.putString(USER_NAME, usuario.text.toString())
                val intent = Intent(this, Welcome::class.java).apply {
                    putExtras(bundle)
                }
                startActivity(intent)
                }
        }
    }

}