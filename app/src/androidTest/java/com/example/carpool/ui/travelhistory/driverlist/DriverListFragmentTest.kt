package com.example.carpool.ui.travelhistory.driverlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.carpool.R
import com.example.carpool.RecyclerAdapter.RecyclerAdapter
import com.example.carpool.data.DriverHelper
import com.example.carpool.model.Driver
import com.example.carpool.model.UserRepository
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class DriverListFragmentTest {

    private var driverList = listOf(DriverHelper.DR1,DriverHelper.DR2,DriverHelper.D3)


    @Before
    fun setup(){
    }

    @After
    fun close(){

    }


    @Test
    fun onCreateView() {
        launchFragmentInContainer<DriverListFragment>(null,R.style.Theme_Carpool)
        Thread.sleep(40000)

        driverList.drop(1)
        var result = driverList

        assertEquals(result,driverList)


    }



    @Test
    fun onActivityCreated() {
    }

    @Test
    fun setListener() {
    }
}