package com.example.carpool.data.models

import android.os.Parcelable
import com.example.carpool.data.models.Vehicle
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Driver (
    val name: String,
    val phone: String,
    val description: String,
    val rating: Float,
    val vehicle: Vehicle,
    val price: Float
) : Parcelable
