package com.example.carpool


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*



class Register : AppCompatActivity() {
    lateinit var registerUser: EditText
    lateinit var registerPassword: EditText
    lateinit var registerButton: Button
    lateinit var registerLoginButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerUser = findViewById(R.id.editTextUsername)
        registerPassword = findViewById(R.id.editTextPassword)
        registerButton = findViewById(R.id.RegisterButton)
        registerLoginButton = findViewById(R.id.registerLogin)

        registerButton.setOnClickListener {
            if (registerUser.text.isEmpty() || registerPassword.text.isEmpty()){
                Toast.makeText(this, "Por favor introduce los datos correctamente", Toast.LENGTH_LONG).show()

                registerUser.error = "Escribe un nombre de usuario"
                registerPassword.error="Escribe una contraseña"

                fun EditText.clearError() {
                    error = null
                }

            }else{
                val tempUsuario = User(registerUser.text.toString(),registerPassword.text.toString())
                if(tempUsuario.addUser()){
                    val bundle =Bundle()
                    Toast.makeText(this, "Se ha registrado con éxito :)", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java).apply{
                  }
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