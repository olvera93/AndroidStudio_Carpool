package com.example.carpool.model

import androidx.lifecycle.LiveData

interface Repository {
    fun getUser(): LiveData<List<Userdbclass>>


    suspend fun insertUser(userdbclass: Userdbclass)
    fun populateUser(user: List<Userdbclass>)
    fun viewProfile(user:String):Userdbclass
    fun removeUser(vararg User:Userdbclass)
}