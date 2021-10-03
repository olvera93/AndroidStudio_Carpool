package com.example.carpool.ui.travelhistory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carpool.R
import com.example.carpool.ui.travelhistory.driverinfo.DetailFragment
import com.example.carpool.ui.travelhistory.driverinfo.DriverInfoActivity
import com.example.carpool.ui.travelhistory.driverlist.DriverListFragment

class TravelHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_travel_history)

        val listFragment = supportFragmentManager.findFragmentById(R.id.fragmentList) as DriverListFragment

        listFragment.setListener{
            val detailFragment = supportFragmentManager.findFragmentById(R.id.fragmentDetail) as? DetailFragment

            // Pantalla grande, mostrar detalle en el fragment
            if(detailFragment!=null){
                detailFragment.showDriver(it)
            } else{ //pantalla pequeña, navegar a un nuevo Activity
                val intent = Intent(this, DriverInfoActivity::class.java)
                intent.putExtra(DriverInfoActivity.DRIVER,it)
                startActivity(intent)
            }
        }
    }



}