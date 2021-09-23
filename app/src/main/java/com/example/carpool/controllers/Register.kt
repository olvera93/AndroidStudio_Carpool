package com.example.carpool.controllers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.carpool.MainActivity
import com.example.carpool.*
import com.example.carpool.model.User
import com.example.carpool.receiver.AirplaneReceiver


class Register : AppCompatActivity() {
    private lateinit var registerUser: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerLoginButton : Button
    private lateinit var registerName : EditText
    private lateinit var registerPhone : EditText


    private var airplaneReceiver = AirplaneReceiver()


    companion object {
        const val CHANNEL_TRAVEL = "CHANNEL_TRAVEL"

        var notificationId = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerUser = findViewById(R.id.edit_userR)
        registerPassword = findViewById(R.id.edit_passwordR)
        registerButton = findViewById(R.id.RegisterButton)
        registerLoginButton = findViewById(R.id.registerLogin)
        registerPhone = findViewById(R.id.edit_phoneR)
        registerName = findViewById(R.id.edit_full_nameR)

        // Para android Oreo en adelante, es obligatorio registrar el canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNotificationChannel()
        }


        // lee la configuración del modo avión
        val isEnabled = Settings.System.getInt(
            contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) == 1

        registerButton.isEnabled = !isEnabled
        registerLoginButton.isEnabled = !isEnabled

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(airplaneReceiver, it)
        }

        registerButton.setOnClickListener {
            if (registerUser.text.isEmpty() || registerPassword.text.isEmpty() ||
                registerName.text.isEmpty() || registerPhone.text.isEmpty()){
                Toast.makeText(this, getString(R.string.incorrect_data), Toast.LENGTH_SHORT).show()

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
                    expandableNotification()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotificationChannel() {
        val name = getString(R.string.channel_travel)
        val descriptionText = getString(R.string.travel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_TRAVEL, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun expandableNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_TRAVEL)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setColor(ContextCompat.getColor(this, R.color.primaryColor))
            .setContentTitle(getString(R.string.simple_title))
            .setContentText(getString(R.string.large_text))
            .setLargeIcon(getDrawable(R.mipmap.carpool)?.toBitmap())
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.large_text)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this).run {
            notify(++notificationId, notification)
        }

    }

    override fun onRestart() {
        super.onRestart()
        val i = Intent(this, Register::class.java)
        startActivity(i)
        finish()
    }
}