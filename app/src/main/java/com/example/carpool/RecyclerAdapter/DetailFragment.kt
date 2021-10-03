package com.example.carpool.RecyclerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.carpool.R
import com.example.carpool.model.Driver

class DetailFragment : Fragment() {

    private lateinit var tvDriverName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var rbRate: RatingBar
    private lateinit var imgTravel: ImageView
    private lateinit var vehicleTextBrand: TextView
    private lateinit var vehicleTextModel: TextView
    private lateinit var vehicleTextPlate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        tvDriverName = view.findViewById(R.id.tvName)
        tvDescription = view.findViewById(R.id.tvDescription)
        rbRate = view.findViewById(R.id.rbRate)
        imgTravel = view.findViewById(R.id.imgDriver)
        vehicleTextBrand = view.findViewById(R.id.vehicleTextBrand)
        vehicleTextModel = view.findViewById(R.id.vehicleTextModel)
        vehicleTextPlate = view.findViewById(R.id.vehicleTextPlate)

        return view
    }

    fun showDriver(driver: Driver){
        view?.visibility = View.VISIBLE
        tvDriverName.text = driver.name
        tvDescription.text = driver.description
        rbRate.rating = driver.rating
        imgTravel.setImageResource(driver.imageTravel)
        vehicleTextBrand.text = driver.vehicle.marca
        vehicleTextModel.text = driver.vehicle.modelo
        vehicleTextPlate.text = driver.vehicle.placas
    }

}
