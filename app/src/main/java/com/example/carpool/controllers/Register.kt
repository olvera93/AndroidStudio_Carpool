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
        registerUser = findViewById(R.id.edit_userR)
        registerPassword = findViewById(R.id.edit_passwordR)
        registerButton = findViewById(R.id.RegisterButton)
        registerLoginButton = findViewById(R.id.registerLogin)
        registerPhone = findViewById(R.id.edit_phoneR)
        registerName = findViewById(R.id.edit_full_nameR)

        var translateLeft = R.anim.translate_left_side

        registerButton.setOnClickListener {
            if (registerUser.text.isEmpty() || registerPassword.text.isEmpty() ||
                registerName.text.isEmpty() || registerPhone.text.isEmpty()){
                Toast.makeText(this, getString(R.string.incorrect_data), Toast.LENGTH_LONG).show()

                registerUser.error = getString(R.string.incorrect_user)
                registerPassword.error= getString(R.string.incorrect_password)
                registerPhone.error= getString(R.string.incorrect_phone)
                registerName.error = getString(R.string.incorrect_fullname)

                fun EditText.clearError() {
                    error = null
                }

            }else{
                val tempUsuario =
                    User(registerName.text.toString(),registerPhone.text.toString(),
                        registerUser.text.toString(), registerPassword.text.toString())
                if(tempUsuario.addUser()){
                    Toast.makeText(this, getString(R.string.successfully_registered), Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userDB",tempUsuario)
                    startActivity(intent)
                    overridePendingTransition(R.anim.translate_left_side, R.anim.translate_left_out)
                }else{
                    Toast.makeText(this,getString(R.string.account_already_registered),Toast.LENGTH_LONG).show()
                    registerUser.setText("")
                    registerPassword.setText("")
                }
            }
        }

        registerLoginButton.setOnClickListener{
            val tempUsuario =
                User("Admin","5555555555",
                    "Admin", "Admin123")
            val prueba=tempUsuario.addUser()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userDB",tempUsuario)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_left_side, R.anim.translate_left_out)

        }

    }
}