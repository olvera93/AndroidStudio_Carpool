package com.example.carpool.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  Vehicle ( var color: String,
                      var marca: String,
                      var modelo:String,
                      var placas: String ): Parcelable
