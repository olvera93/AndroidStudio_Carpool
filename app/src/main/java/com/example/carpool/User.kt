package com.example.carpool

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

var database =  mutableListOf(listOf("adminName","adminPhone","adminUser","123"))
@Parcelize
class User(val name: String, val phone: String, val user: String,
           val password:String):Parcelable{

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

