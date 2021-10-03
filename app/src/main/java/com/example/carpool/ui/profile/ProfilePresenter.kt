package com.example.carpool.ui.profile

import android.content.Intent
import android.view.View
import com.example.carpool.data.models.User
import com.example.carpool.data.room.UserDb
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProfilePresenter(view: View){
    var user = User()

    fun updateName(name: String){
        user.name = name
    }

    fun updatePhone(phone: String){
        user.phone = phone
    }

    fun updateUser(usuario: String){
        user.user = usuario
    }

    fun updatePassword(contra: String){
        user.password = contra
    }

    fun dbOperationUpdate(activity: ProfileActivity) {
        //Validacion si existe usuario en la base de datos de la clase User
        val nameu=user.name
        val usuu=user.user
        val pasu=user.password
        val phoneu=user.phone

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            val Userdbclass = UserDb
                .getInstance(activity)
                ?.userDao()
            Userdbclass?.apply {updateUser(usuu,nameu,phoneu,pasu)}
        })}

    interface View{
        fun dbOperationUpdate()
    }
}