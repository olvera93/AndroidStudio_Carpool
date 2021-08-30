package com.example.carpool.RecyclerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carpool.R
import com.example.carpool.model.Driver
import com.example.carpool.model.Vehicle
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var mAdapter : RecyclerAdapter
    private var listener : (Driver) ->Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
    }

    fun setListener(l: (Driver) ->Unit){
        listener = l
    }

    //configuramos lo necesario para desplegar el RecyclerView
    private fun setUpRecyclerView(){
        recyclerDrivers.setHasFixedSize(true)
        recyclerDrivers.layoutManager = LinearLayoutManager(activity)
        //seteando el Adapter
        mAdapter = RecyclerAdapter( requireActivity(), getProducts(), listener)
        //asignando el Adapter al RecyclerView
        recyclerDrivers.adapter = mAdapter
    }

    //generamos datos dummy con este método
    private fun getProducts(): MutableList<Driver>{
        var drivers:MutableList<Driver> = ArrayList()

        drivers.add(Driver("Javier", "5511223344","Conducto desde hace mas de 10 años", 4.5f, Vehicle("Azul", "Nissan", "Versa", "AU219"),345.00f))
        drivers.add( Driver("Juan", "5522334455","Conducto desde hace mas de 2 años", 3.8f, Vehicle("Azul", "Nissan", "Versa", "AU219"),276.45f))
        drivers.add(Driver("Luis", "5533445566", "Conducto desde hace mas de 4 años",2.4f, Vehicle("Azul", "Nissan", "Versa", "AU219"),289.99f))
        drivers.add(Driver("Fernanda", "5544556677", "Conducto desde hace mas de 7 años",4.0f, Vehicle("Azul", "Nissan", "Versa", "AU219"),126.43f))
        drivers.add(Driver("Luisa", "5555667788", "Conducto desde hace mas de 9 años",4.9f, Vehicle("Azul", "Nissan", "Versa", "AU219"),179.70f))
        drivers.add(Driver("Manuel", "5566778899", "Conducto desde hace mas de 1 año",3.9f, Vehicle("Azul", "Nissan", "Versa", "AU219"),157.87f))
        drivers.add(Driver("Jose", "5577889900", "Conducto desde hace mas de 5 años",4.3f, Vehicle("Azul", "Nissan", "Versa", "AU219"),132.39f))
        drivers.add(Driver("Maria", "5588990011", "Conducto desde hace mas de 11 años",2.0f, Vehicle("Azul", "Nissan", "Versa", "AU219"),118.89f))
        drivers.add(Driver("Ana", "5599001122", "Conducto desde hace mas de 6 años",4.8f, Vehicle("Azul", "Nissan", "Versa", "AU219"),99.34f))

        return drivers
    }
}




