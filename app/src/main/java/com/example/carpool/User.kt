package com.example.carpool

class User(val user: String,val password:String){
    val database : MutableMap<String,String> = mutableMapOf("admin" to "123")

    fun addUser():Boolean{
        return if(!database.containsKey(user)){
            database[user] = password
            true
        }else{
            false
        }
    }
}