package com.example.carpool.ui.register


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.carpool.R
import com.example.carpool.data.models.User

import com.example.carpool.data.room.UserDb
import com.example.carpool.data.room.Userdbclass
import com.example.carpool.MainActivity
import com.example.carpool.ui.receiver.AirplaneReceiver

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class RegisterActivity : AppCompatActivity(), RegisterPresenter.View {
    private lateinit var registerUser: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerLoginButton : Button
    private lateinit var registerName : EditText
    private lateinit var registerPhone : EditText

    private val presenter = RegisterPresenter(this)
    private var airplaneReceiver = AirplaneReceiver()

    companion object {
        const val CHANNEL_TRAVEL = "CHANNEL_TRAVEL"
        const val CHANNEL_FIREBASE = "CHANNEL_FIREBASE"
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

        registerUser.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updateUser(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        registerPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updatePassword(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        registerName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updateName(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        registerPhone.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updatePhone(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        // Para android Oreo en adelante, es obligatorio registrar el canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNotificationChannel()
            setNotificationChannelFirebase()
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

            }else if (registerPhone.text.length < 10){
                Toast.makeText(this, getString(R.string.phone_number), Toast.LENGTH_SHORT).show()
            } else {
                //Check if register exists
                checkRegister()
            }
        }

        registerLoginButton.setOnClickListener{
            addExistingUsersToDB()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotificationChannelFirebase(){
        val channel = NotificationChannel(
            CHANNEL_FIREBASE,
            "Canal Generico",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "CANAL FIREBASE"
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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


    fun checkRegister(){
        val user:String?
        user=registerUser.text.toString()

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        var usercomp: String?
        executor.execute(Runnable {
            usercomp= UserDb
                .getInstance(this)
                ?.userDao()
                ?.validationUser(user)
            if(user==usercomp){
                runOnUiThread(Runnable {
                    Toast.makeText(this, getString(R.string.account_already_registered)
                        , Toast.LENGTH_SHORT).show()})
            }
            else{


            }
        })

    }

    override fun onStart() {
        super.onStart()
        if (!isLocationEnabled()) {
            goToTurnLocation()
        }
    }

    private fun goToTurnLocation(){
        Toast.makeText(this, "Debes prender el servicio de GPS", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }


    //checa si el gps está apagado
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRestart() {
        super.onRestart()
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun addExistingUsersToDB() {
        presenter.addExistingUsersToDB(this)
    }

    override fun addNewUserToDB() {
        presenter.addNewUserToDB(this)
    }

}