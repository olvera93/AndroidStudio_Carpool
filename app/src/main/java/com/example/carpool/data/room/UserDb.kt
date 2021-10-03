package com.example.carpool.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Userdbclass::class),version = 2)
abstract class UserDb: RoomDatabase(){

    companion object{
        private var userInstance: UserDb? = null
        const val DB_NAME ="User_DB"

        fun getInstance(context: Context): UserDb?{
            if (userInstance ==null){
                synchronized(UserDb::class){
                    userInstance =Room.databaseBuilder(
                        context.applicationContext,
                        UserDb::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                }
            }
            return userInstance
        }
    }
    abstract fun userDao(): DAO_User
}