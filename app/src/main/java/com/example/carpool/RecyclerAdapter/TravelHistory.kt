package com.example.carpool.RecyclerAdapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.carpool.R
import com.example.carpool.model.Driver

class TravelHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_travel_history)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.adapter = RecyclerAdapter(listOf(
            Driver("Javier", "5511223344", 4.5f),
            Driver("Juan", "5522334455", 3.8f),
            Driver("Luis", "5533445566", 2.4f),
            Driver("Fernanda", "5544556677", 4.0f),
            Driver("Luisa", "5555667788", 4.9f),
            Driver("Manuel", "5566778899", 3.9f),
            Driver("Jose", "5577889900", 4.3f),
            Driver("Maria", "5588990011", 2.0f),
            Driver("Ana", "5599001122", 4.8f),
        ))


    }



}