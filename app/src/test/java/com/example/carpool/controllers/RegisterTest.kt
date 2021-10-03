package com.example.carpool.controllers

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.carpool.CoroutineTestRule
import com.example.carpool.data.room.UserViewModel
import com.example.carpool.model.*
import com.google.common.truth.ExpectFailure.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test




class RegisterTest : TestCase(){



    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var userRepository:Repository
    private lateinit var viewModel: UserViewModel

    private val user1 = Userdbclass(0,"USR1","ASD1","5556787654","USER1")
    private val user2 = Userdbclass(1,"USR2","ASD1","5556787654","USER2")
    private val user3 = Userdbclass(2,"USR3","ASD1","5556787654","USER2")

    @Before
    fun setup(){
        userRepository=FakeRepository()

        val users = listOf(user1,user2,user3)
        userRepository.populateUser(users)
        viewModel = UserViewModel(userRepository)
    }
    @Test
    fun remove_User_User(){
        val observer = Observer<List<Userdbclass>>{}

        try {

            viewModel.userlist.observeForever(observer)

            // When: Cuando probamos agregar un nuevo evento con nuestro ViewModel
            viewModel.removeUser(user1)

            //Then: Entonces el evento fue disparado (eso provoca que no sea nulo y que tenga alguno de los estados:
            //      loading, success, error)
            val users = viewModel.userlist.value

            assertEquals(user1,users)

        } finally {
            viewModel.userlist.removeObserver(observer) // eliminamos el observer para evitar memory leaks
        }
        viewModel.removeUser(user1)
    }

    @Test
    suspend fun Insert_Usernotnull()
    {
        userRepository.insertUser(user1)
        assertEquals(user1.User,"USER1")
    }


    @Test
    fun checkRegister() {

    }

    @Test
    fun onCreate() {
    }

    @Test
    fun onStart() {
    }

    @Test
    fun onRestart() {
    }


}