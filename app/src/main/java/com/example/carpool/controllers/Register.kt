package com.example.carpool.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.carpool.MainActivity
import com.example.carpool.R
import com.example.carpool.model.User

class Register : AppCompatActivity() {
    lateinit var registerUser: EditText
    lateinit var registerPassword: EditText
    lateinit var registerButton: Button
    lateinit var registerLoginButton : Button
    lateinit var registerName : EditText
    lateinit var registerPhone : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerName = findViewById(R.id.editTextFullName)
        registerUser = findViewById(R.id.editTextUsername)
        registerPassword = findViewById(R.id.editTextPassword)
        registerButton = findViewById(R.id.RegisterButton)
        registerLoginButton = findViewById(R.id.registerLogin)
        registerPhone = findViewById(R.id.editTextPhoneN)

        registerButton.setOnClickListener {
            if (registerUser.text.isEmpty() || registerPassword.text.isEmpty() ||
                registerName.text.isEmpty() || registerPhone.text.isEmpty()){
                Toast.makeText(this, "Por favor introduce los datos correctamente", Toast.LENGTH_LONG).show()

                registerUser.error = "Escribe un nombre de usuario"
                registerPassword.error="Escribe una contraseña"
                registerPhone.error="Escribe un número de teléfono"
                registerName.error = "Escribe un nombre completo"

                fun EditText.clearError() {
                    error = null
                }

            }else{
                val tempUsuario =
                    User(registerName.text.toString(),registerPhone.text.toString(),
                        registerUser.text.toString(), registerPassword.text.toString())
                if(tempUsuario.addUser()){
                    Toast.makeText(this, "Se ha registrado con éxito :)", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userDB",tempUsuario)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"Esa cuenta ya está registrada :(",Toast.LENGTH_LONG).show()
                    registerUser.setText("")
                    registerPassword.setText("")
                }
            }
        }

        registerLoginButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}