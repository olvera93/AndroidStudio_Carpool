package com.example.carpool.data

import android.content.Context

import com.example.carpool.model.Repository
import com.example.carpool.model.UserDb
import com.example.carpool.model.UserRepository

object ServiceLocator {

    private var database: UserDb? = null
    var repository: Repository? = null

    private val lock = Any()

    fun resetRepository(){

        synchronized(lock){
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            repository = null
        }

    }

    fun provideRepository(context: Context): Repository{
        synchronized(lock){
            return repository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): Repository{
        database = UserDb.getInstance(context)
        val repo = UserRepository(database!!.userDao())
        repository = repo
        return repo
    }

}
