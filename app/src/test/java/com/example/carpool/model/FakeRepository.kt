package com.example.carpool.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeRepository:Repository {

    private var observableUsers = MutableLiveData<List<Userdbclass>>()
    override fun getUser(): LiveData<List<Userdbclass>> {
        return observableUsers
    }

    override suspend fun insertUser(userdbclass: Userdbclass) {
        val newList: MutableList<Userdbclass> = observableUsers.value?.toMutableList()?: mutableListOf()
        newList.add(userdbclass)
        observableUsers.value=newList
    }

    override fun populateUser(user: List<Userdbclass>) {
        observableUsers.value=user
    }

    override fun viewProfile(user: String): Userdbclass {
        TODO("Not yet implemented")
    }

    override fun removeUser(vararg User: Userdbclass) {
        TODO("Not yet implemented")
    }
}