package com.example.carpool.model
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*


class UserRepository (
    private val DAO_User:DAO_User,
    private val ioDispatcher: CoroutineDispatcher  = Dispatchers.IO):Repository
{
    override fun getUser(): LiveData<List<Userdbclass>> {
        return DAO_User.getUser()
    }


    override suspend fun insertUser(userdbclass: Userdbclass) {
        coroutineScope {
            launch { DAO_User.insertUser(userdbclass) }
        }
    }

    override fun populateUser(user: List<Userdbclass>) {
        return DAO_User.insertAll(user)
    }

    override fun viewProfile(user: String):Userdbclass {
        return DAO_User.viewProfile(user)
    }

    override fun removeUser(vararg User: Userdbclass) {
        TODO("Not yet implemented")
    }

}