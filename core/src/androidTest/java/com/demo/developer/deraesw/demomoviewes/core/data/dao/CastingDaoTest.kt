package com.demo.developer.deraesw.demomoviewes.core.data.dao

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.room.Room
//import androidx.test.InstrumentationRegistry
//import androidx.test.runner.AndroidJUnit4
//import com.demo.developer.deraesw.demomoviewes.data.LiveDataTestUtil
//import com.demo.developer.deraesw.demomoviewes.data.appDatabase
//import junit.framework.Assert.assertFalse
//import junit.framework.Assert.assertTrue
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class CastingDaoTest {
//
//    @JvmField
//    @Rule
//    val testRule = InstantTaskExecutorRule()
//
//    lateinit var database: appDatabase
//    lateinit var peopleDAO: PeopleDAO
//    lateinit var movieDAO: MovieDAO
//    lateinit var castingDAO: CastingDAO
//
//    @Before
//    fun initDb(){
//        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), appDatabase::class.java)
//                .allowMainThreadQueries()
//                .build()
//
//        peopleDAO = database.peopleDAO()
//        castingDAO = database.castingDAO()
//        movieDAO = database.movieDAO()
//    }
//
//    @Test
//    fun closeDb(){
//        database.close()
//    }
//
//    fun insertCastingList_withoutPeopleNeitherMovie(){
//
//    }
//
//    fun insertCastingList_withoutPeople(){
//
//    }
//
//    fun insertCastingList_withoutMovie(){
//
//    }
//
//    fun insertCastingList_fetchCasting(){
//
//    }
//
//    fun insertCastingList_deleteCasting(){
//
//    }
//}