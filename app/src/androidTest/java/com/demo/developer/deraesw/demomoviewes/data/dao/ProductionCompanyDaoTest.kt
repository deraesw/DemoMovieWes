package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.getValue
import com.demo.developer.deraesw.demomoviewes.utils.DataTestUtils
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductionCompanyDaoTest : BaseDaoTest() {

    private lateinit var productionCompanyDao: ProductionCompanyDao

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    override fun initDb() {
        super.initDb()
        return runBlocking {
            productionCompanyDao = database.productionCompanyDao()
        }
    }

    @Test
    fun testSelectOnEmptyTable() {
        val list = getValue(productionCompanyDao.selectAll())
        assertThat(list.isEmpty(), equalTo(true))
    }

    @Test
    fun testBulkInsertTable() = runBlocking {
        productionCompanyDao.bulkForceInsert(DataTestUtils.productionList)

        val list = getValue(productionCompanyDao.selectAll())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.productionList.size))
        assertThat(list[0], equalTo(DataTestUtils.productionList[0]))
        assertThat(list[1], equalTo(DataTestUtils.productionList[1]))
        assertThat(list[2], equalTo(DataTestUtils.productionList[2]))
    }

    @Test
    fun testBulkInsertTableWithDuplicate() = runBlocking {
        productionCompanyDao.bulkForceInsert(DataTestUtils.productionList)
        productionCompanyDao.bulkForceInsert(DataTestUtils.productionListWithDuplicate)
        val list = getValue(productionCompanyDao.selectAll())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.productionListWithDuplicate.size))
        assertThat(list[0], equalTo(DataTestUtils.productionListWithDuplicate[0]))
        assertThat(list[1], equalTo(DataTestUtils.productionListWithDuplicate[1]))
        assertThat(list[2], equalTo(DataTestUtils.productionListWithDuplicate[2]))
    }

    @Test
    fun testDeleteAllData() = runBlocking {
        productionCompanyDao.bulkForceInsert(DataTestUtils.productionList)
        val list = getValue(productionCompanyDao.selectAll())
        assertThat(list.isEmpty(), equalTo(false))

        productionCompanyDao.deleteAll()
        val emptyList = getValue(productionCompanyDao.selectAll())
        assertThat(emptyList.isEmpty(), equalTo(true))
    }

    @Test
    fun testDeleteObsolete() = runBlocking {
        productionCompanyDao.bulkForceInsert(DataTestUtils.productionList)

        productionCompanyDao.removeObsoleteProduction("10-01-2019")
        val list = getValue(productionCompanyDao.selectAll())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo((DataTestUtils.productionList.size - 1)))
        assertFalse(list[0] == DataTestUtils.productionList[0])
    }
}