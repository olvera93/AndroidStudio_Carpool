package com.example.carpool.model

import androidx.room.*

@Dao
interface DAO_User {


    @Insert
    fun insertUser(User: Userdbclass)


    @Delete
    fun removeUser(User: Userdbclass)

    @Query("DELETE FROM Userdbclass WHERE Id_user=:id")
    fun RemoveuserbyID(id: Int)

    @Delete
    fun removeUser(vararg User: Userdbclass)

    @Query("SELECT * FROM Userdbclass")
    fun getUser(): List<Userdbclass>

    @Query("SELECT * FROM Userdbclass WHERE Id_user = :id")
    fun getUserbyid(id: Int): Userdbclass

    @Query("SELECT * FROM Userdbclass WHERE User= :user")
    fun checkRegister(user:String):List<Userdbclass>

    @Query("SELECT * FROM Userdbclass WHERE User= :user")
    fun viewProfile(user:String):Userdbclass

    @Query("SELECT user FROM Userdbclass WHERE User= :user")
    fun validationUser(user:String):String
    @Query("UPDATE Userdbclass set  Name=:name, Phone=:phone, Password=:password where user=:user")
    fun updateUser(user: String,name:String,phone:String,password:String)

}

