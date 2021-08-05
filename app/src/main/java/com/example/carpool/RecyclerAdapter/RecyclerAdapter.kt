package com.example.carpool.RecyclerAdapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carpool.model.Driver
import com.example.carpool.R

class RecyclerAdapter(val drivers : List<Driver>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val name = view.findViewById<TextView>(R.id.tvNombre)
        private val phone = view.findViewById<TextView>(R.id.tvPhone)
        private val ratingBar = view.findViewById<RatingBar>(R.id.rbCalificacion)

        fun bind(driver: Driver){
            name.text = driver.name
            phone.text = driver.phone
            ratingBar.rating = driver.rating
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_driver, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = drivers[position]
        holder.bind(contact)
    }


    override fun getItemCount(): Int {
        return drivers.size
    }

}