package com.example.carpool.model

import androidx.room.*

@Dao
interface DAO_User {


    @Insert
    fun insertUser(User: Userdbclass)

    @Update()//"UPDATE Userdbclass set User=:User, Password=:Password, Phone=:Phone, Name=:Name WHERE User=:User")
    fun updateUser(User: Userdbclass)

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

    @Query("SELECT User FROM Userdbclass WHERE User= :user")
    fun checkRegister(user:String):String
}
