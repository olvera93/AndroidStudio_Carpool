package com.example.carpool.data.room
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carpool.model.Repository
import com.example.carpool.model.Userdbclass
import com.example.carpool.model.UserRepository
import com.example.carpool.util.notifications.getNumberofusers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository:Repository): ViewModel() {


    val userlist = userRepository.getUser()
    var users = MutableLiveData(0)
    var ids = MutableLiveData(0)

    private var _edituser = MutableLiveData<Int?>()
    val eventeditUser = _edituser

    fun updateUser(user:List<Userdbclass>){
        users.value = getNumberofusers(user)

    }


    fun removeUser(user: Userdbclass) = viewModelScope.launch{
        userRepository.removeUser(user)
    }

    fun viewProfile(editUser: Int){
        eventeditUser.value=editUser
    }

}