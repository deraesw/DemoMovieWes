package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.developer.deraesw.demomoviewes.data.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
open class BaseDaoTest {

    protected lateinit var database: AppDatabase

    @Before
    open fun initDb() {
        return runBlocking {
            database = Room
                .inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    AppDatabase::class.java
                )
                .build()
        }
    }

    @Test
    open fun closeDb() {
        database.close()
    }
}