package com.example.carpool.RecyclerAdapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carpool.R
import com.example.carpool.model.Driver

class DetailActivity : AppCompatActivity() {

    companion object {
        val DRIVER = "DRIVER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val driver = intent.getParcelableExtra<Driver>(DRIVER)
        val detailFragment = supportFragmentManager.findFragmentById(R.id.fragmentDetail) as? DetailFragment
        if (driver != null) {
            detailFragment?.showDriver(driver)
        }

    }
}
