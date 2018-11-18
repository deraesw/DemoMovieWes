package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.LiveDataTestUtil
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductionCompanyDaoTest : BaseDaoTest(){

    lateinit var productionCompanyDao: ProductionCompanyDao

    override fun initDb(){
        super.initDb()
        productionCompanyDao = database.productionCompanyDao()
    }

    @Test
    fun selectAll_onEmptyTable(){
        val list = LiveDataTestUtil.getValue(productionCompanyDao.selectAll())
        assertTrue(list.isEmpty())
    }


}