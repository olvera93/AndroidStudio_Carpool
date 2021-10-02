package com.example.carpool.RecyclerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carpool.R
import com.example.carpool.model.Driver

//Declaración con constructor
class RecyclerAdapter(
    private val context:Context,
    private val drivers: MutableList<Driver>,
    private val clickListener: (Driver) -> Unit): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //Aquí atamos el ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val driver = drivers.get(position)
        holder.bind(driver, context)

        holder.view.setOnClickListener{clickListener(driver)}

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_driver, parent, false))
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    //El ViewHolder ata los datos del RecyclerView a la Vista para desplegar la información
    //También se encarga de gestionar los eventos de la View, como los clickListeners
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        //obteniendo las referencias a las Views
        private val name = view.findViewById<TextView>(R.id.tvNombre)
        private val price = view.findViewById<TextView>(R.id.tvPrice)


        //"atando" los datos a las Views
        fun bind(driver: Driver, context: Context){
            name.text = driver.name
            price.text = "$"+driver.price.toString()
        }
    }

}
