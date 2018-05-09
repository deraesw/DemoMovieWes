package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseDaoTest {
    @JvmField
    @Rule
    val testRule = InstantTaskExecutorRule()

    lateinit var database: appDatabase

    @Before
    open fun initDb(){
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), appDatabase::class.java)
                .allowMainThreadQueries()
                .build()

    }

    @After
    fun closeDb(){
        database.close()
    }
}