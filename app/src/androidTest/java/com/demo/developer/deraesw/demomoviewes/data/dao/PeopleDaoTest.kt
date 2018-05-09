package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.LiveDataTestUtil
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PeopleDaoTest {

    @JvmField
    @Rule
    val testRule = InstantTaskExecutorRule()

    lateinit var database: appDatabase
    lateinit var peopleDAO: PeopleDAO

    @Before
    fun initDb(){
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), appDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        peopleDAO = database.peopleDAO()
    }

    @Test
    fun closeDb(){
        database.close()
    }

    @Test
    fun selectOnEmptyTable(){
        val list = LiveDataTestUtil.getValue(peopleDAO.selectAllPeople())
        assertTrue(list.isEmpty())
    }

    @Test
    fun insertListPeopleAndSelectAll(){
        peopleDAO.bulkInsertPeoples(DataTestUtils.peopleList)
        val list = LiveDataTestUtil.getValue(peopleDAO.selectAllPeople())

        assertTrue(list.isNotEmpty())
        assertTrue(list.size == DataTestUtils.peopleList.size)
    }

    @Test
    fun insertListWithDuplicate(){
        peopleDAO.bulkInsertPeoples(DataTestUtils.peopleList)
        val list = LiveDataTestUtil.getValue(peopleDAO.selectAllPeople())

        assertTrue(list.isNotEmpty())
        assertTrue(list.size == DataTestUtils.peopleList.size)

        val duplicateItem = listOf(DataTestUtils.people2Duplicate)
        peopleDAO.bulkInsertPeoples(duplicateItem)

        val listDuplicate = LiveDataTestUtil.getValue(peopleDAO.selectAllPeople())

        assertTrue(listDuplicate.isNotEmpty())
        assertTrue(listDuplicate.size == DataTestUtils.peopleList.size)
        assertFalse(listDuplicate[1].name == list[1].name)
        assertFalse(listDuplicate[1].gender == list[1].gender)
        assertTrue(listDuplicate[1].name == DataTestUtils.people2Duplicate.name)
        assertTrue(listDuplicate[1].gender == DataTestUtils.people2Duplicate.gender)
    }

    @Test
    fun insertListAndDeleteAll(){
        peopleDAO.bulkInsertPeoples(DataTestUtils.peopleList)
        val list = LiveDataTestUtil.getValue(peopleDAO.selectAllPeople())

        assertTrue(list.isNotEmpty())
        assertTrue(list.size == DataTestUtils.peopleList.size)

        peopleDAO.deleteAll()
        val listDelete = LiveDataTestUtil.getValue(peopleDAO.selectAllPeople())

        assertTrue(listDelete.isEmpty())
        assertFalse(listDelete.size == DataTestUtils.peopleList.size)
        assertFalse(listDelete.size == list.size)
    }
}