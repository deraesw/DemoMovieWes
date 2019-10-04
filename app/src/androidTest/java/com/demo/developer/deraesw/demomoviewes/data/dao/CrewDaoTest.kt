package com.demo.developer.deraesw.demomoviewes.data.dao
//
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
//class CrewDaoTest {
//
//    @JvmField
//    @Rule
//    val testRule = InstantTaskExecutorRule()
//
//    lateinit var database: appDatabase
//    lateinit var peopleDAO: PeopleDAO
//    lateinit var movieDAO: MovieDAO
//    lateinit var crewDAO: CrewDAO
//
//    @Before
//    fun initDb(){
//        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), appDatabase::class.java)
//                .allowMainThreadQueries()
//                .build()
//
//        peopleDAO = database.peopleDAO()
//        crewDAO = database.crewDAO()
//        movieDAO = database.movieDAO()
//    }
//
//    @Test
//    fun closeDb(){
//        database.close()
//    }
//
//    fun insertCrewList_withoutPeopleNeitherMovie(){
//
//    }
//
//    fun insertCrewList_withoutPeople(){
//
//    }
//
//    fun insertCrewList_withoutMovie(){
//
//    }
//
//    fun insertCrewList_fetchCrew(){
//
//    }
//
//    fun insertCrewList_deleteCrew(){
//
//    }
//}