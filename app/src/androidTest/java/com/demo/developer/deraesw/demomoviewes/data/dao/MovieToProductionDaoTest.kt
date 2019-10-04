package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.data.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieToProductionDaoTest {

    private lateinit var database: appDatabase
    private lateinit var movieToProductionDao: MovieToProductionDao
    private lateinit var movieDAO: MovieDAO
    lateinit var productionCompanyDao: ProductionCompanyDao

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        return runBlocking {
            database = Room.inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    appDatabase::class.java)
                    .build()

            movieToProductionDao = database.movieToProductionDao()

            productionCompanyDao = database.productionCompanyDao()
            movieDAO = database.movieDAO()

            movieDAO.bulkInsertMovies(DataTestUtils.movieList)
            productionCompanyDao.bulkForceInsert(DataTestUtils.productionList)
        }
    }

    @After
    fun closeDb(){
        database.close()
    }

    @Test
    fun testBulkInsertTable(){
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        val list = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieToProductionList.filter { it.idMovie == DataTestUtils.movie1.id}.count()))
    }

    @Test
    fun testDeleteCascadeMovie(){
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        val list = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieToProductionList.filter { it.idMovie == DataTestUtils.movie1.id}.count()))

        movieDAO.removeAllMovies()
        val listEmpty = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(listEmpty.isEmpty(), equalTo(true))
    }

    @Test
    fun testDeleteAll(){
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        movieToProductionDao.removeAll()
        val listEmpty = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(listEmpty.isEmpty(), equalTo(true))
    }

    @Test
    fun testBulkInsertDuplicate(){
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        val list = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieToProductionList.filter { it.idMovie == DataTestUtils.movie1.id}.count()))
    }
}