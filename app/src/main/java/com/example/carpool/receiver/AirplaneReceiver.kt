package com.example.carpool.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings.System.getString
import android.widget.Toast
import com.example.carpool.R

class AirplaneReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {


        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return
        // comprobar si el modo avión está habilitado o no
        if (isAirplaneModeEnabled) {
            // Muestra el mensaje si el modo avión está habilitado
            Toast.makeText(context, R.string.airplane_mode_on, Toast.LENGTH_SHORT).show()
        } else {
            // Muestra el mensaje si el modo avión está deshabilitado
            Toast.makeText(context, R.string.airplane_mode_off, Toast.LENGTH_LONG).show()
        }
    }


}