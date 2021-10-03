package com.example.carpool.util.notifications

import com.example.carpool.model.UserDb
import com.example.carpool.model.Userdbclass

internal fun getName(userdbclass: Userdbclass):String{
    return userdbclass.User?:"without user"
}

internal fun getLastName(userdbclass: Userdbclass):String{
    return userdbclass.Name?:"without name"
}

internal fun getPhone(userdbclass: Userdbclass):String{
    return userdbclass.Phone?:"without phone"
}

internal fun getId(userdbclass: Userdbclass):Int{
    return userdbclass.Id_user?:0
}
internal fun getNumberofusers(userdbclass: List<Userdbclass>):Int{
    return userdbclass?.size ?:0
}