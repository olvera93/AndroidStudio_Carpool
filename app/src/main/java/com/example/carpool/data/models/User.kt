package com.example.carpool.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

var database =  mutableListOf(listOf("adminName","adminPhone","adminUser","123"))
@Parcelize
class User(
    var name: String="NOMBRE_DEFAULT", var phone: String="CELULAR_DEFAULT", var user: String="USUARIO_DEFAULT",
    var password:String="CONTRA_DEFAULT"):Parcelable{

    fun addUser():Boolean{
        var users: MutableList<String> = mutableListOf()
        for(user in database){
            users.add(user[2])
        }
        if(this.user in users){
            return false
        }
        database.add(listOf(name,phone,user,password))
        return true
    }

    fun validateUser(usuario:String,contra:String):Boolean{
        for(user in database){
            if(user[2]==usuario && user[3]==contra){
                return true
            }
        }
        return false
    }


    }

