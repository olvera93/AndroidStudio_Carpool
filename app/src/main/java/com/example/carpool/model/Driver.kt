package com.example.carpool.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Driver (
    val name: String,
    val phone: String,
    val description: String,
    val rating: Float,
    val vehicle: Vehicle,
    val price: Float,
    val imageTravel: Int
) : Parcelable
