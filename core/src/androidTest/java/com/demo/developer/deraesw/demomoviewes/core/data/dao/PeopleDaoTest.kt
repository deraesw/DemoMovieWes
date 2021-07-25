package com.demo.developer.deraesw.demomoviewes.core.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.core.data.getValue
import com.demo.developer.deraesw.demomoviewes.core.utils.DataTestUtils
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PeopleDaoTest : BaseDaoTest() {

    private lateinit var peopleDAO: PeopleDAO

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    override fun initDb() {
        super.initDb()
        return runBlocking {
            peopleDAO = database.peopleDAO()
        }
    }

    @Test
    fun testSelectOnEmptyTable() {
        val list = getValue(peopleDAO.selectAllPeople())
        assertThat(list.isEmpty(), equalTo(true))
    }

    @Test
    fun testBulkInsert() = runBlocking {
        peopleDAO.bulkForceInsert(DataTestUtils.peopleList)
        val list = getValue(peopleDAO.selectAllPeople())

        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.peopleList.size))
        assertThat(list[0], equalTo(DataTestUtils.peopleList[0]))
        assertThat(list[1], equalTo(DataTestUtils.peopleList[1]))
    }

    @Test
    fun testBulkInsertWithDuplicate() = runBlocking {
        peopleDAO.bulkForceInsert(DataTestUtils.peopleList)
        val list = getValue(peopleDAO.selectAllPeople())

        peopleDAO.bulkForceInsert(DataTestUtils.peopleListWithDuplicate)
        val listDuplicate = getValue(peopleDAO.selectAllPeople())

        assertThat(listDuplicate.isEmpty(), equalTo(false))
        assertThat(listDuplicate.size, equalTo(DataTestUtils.peopleListWithDuplicate.size))
        assertThat(listDuplicate[0], equalTo(DataTestUtils.peopleListWithDuplicate[0]))
        assertThat(listDuplicate[1], equalTo(DataTestUtils.peopleListWithDuplicate[1]))
        assertFalse(listDuplicate[1] == list[1])
    }

    @Test
    fun testDeleteAll() = runBlocking {
        peopleDAO.bulkForceInsert(DataTestUtils.peopleList)
        val list = getValue(peopleDAO.selectAllPeople())
        assertThat(list.isEmpty(), equalTo(false))

        peopleDAO.deleteAll()
        val listDelete = getValue(peopleDAO.selectAllPeople())
        assertThat(listDelete.isEmpty(), equalTo(true))
    }

    @Test
    fun testDeleteObsolete() = runBlocking {
        peopleDAO.bulkForceInsert(DataTestUtils.peopleList)
        val list = getValue(peopleDAO.selectAllPeople())
        assertThat(list.isEmpty(), equalTo(false))

        peopleDAO.removeObsoletePeople("10-01-2019")
        val listDelete = getValue(peopleDAO.selectAllPeople())

        assertThat(listDelete.isEmpty(), equalTo(false))
        assertThat(listDelete.size, equalTo((DataTestUtils.peopleList.size - 1)))
        assertFalse(listDelete[0] == list[0])
    }
}