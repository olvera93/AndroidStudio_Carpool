package com.example.carpool.ui.travelhistory.driverlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.carpool.R
import com.example.carpool.RecyclerAdapter.RecyclerAdapter
import com.example.carpool.data.DriverHelper
import com.example.carpool.model.Driver
import com.example.carpool.model.UserRepository
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class DriverListFragmentTest {

    private var driverList = listOf(DriverHelper.DR1,DriverHelper.DR2,DriverHelper.D3)


    @Before
    fun setup(){
    }

    @After
    fun close(){

    }


    @Test
    fun onCreateView() {
        launchFragmentInContainer<DriverListFragment>(null,R.style.Theme_Carpool)
        Thread.sleep(40000)
        fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
            val driver = driverList.get(position)
            holder.bind(driver, this)

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
        Espresso.onView(withId(R.id.recyclerDrivers)
            .perfom(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(
                        ViewMatchers.withText("Javier")
                    ),
                    ItemAction(R.id.detailView)
                )
            )

    }



    @Test
    fun onActivityCreated() {
    }

    @Test
    fun setListener() {
    }
}