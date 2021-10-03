package com.example.carpool.util.notifications

import com.example.carpool.data.models.User
import com.example.carpool.data.room.Userdbclass
import junit.framework.TestCase
import org.junit.Assert.*
import org.junit.Test

class RegisterUtilsKtTest : TestCase() {

    @Test
    fun testTestGetName_null_returnNull() {
        val userdbclass = User()
        val result = userdbclass.user
        assertEquals("USUARIO_DEFAULT", result)

    }

    @Test
    fun validateUser_notNull() {
        val userdbclass = User("RAUL", "7263847234", "RAL12", "ASD123")
        val result = userdbclass.user
        assertEquals("RAl12", result)
    }

    @Test
    fun validateName_notNull() {
        val userdbclass = User("RAUL", "7263847234", "RAL12", "ASD123")
        val result = userdbclass.name
        assertEquals("RAUL", result)
    }

    @Test
    fun testGetLastName_null_returnNull() {
        val userdbclass = User()
        val result = userdbclass.name
        assertEquals("NOMBRE_DEFAULT", result)
    }

    @Test
    fun testGetPhone_null_returnNull() {
        val userdbclass = User()
        val result = userdbclass.phone
        assertEquals("CELULAR_DEFAULT", result)
    }

  @Test
    fun validateUser_Exists() {
        val userdbclass = User("RAUL", "7263847234", "RAL12", "ASD123")
        val result = userdbclass.addUser()
        assertEquals(true, result)
    }


}