package com.example.carpool.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index(value =["User"],unique = true)])
data class Userdbclass(
    @PrimaryKey (autoGenerate = true) val Id_user: Int=0,
    @ColumnInfo val User: String?,
    @ColumnInfo val Password: String?,
    @ColumnInfo val Phone:String?,
    @ColumnInfo val Name: String?
)

