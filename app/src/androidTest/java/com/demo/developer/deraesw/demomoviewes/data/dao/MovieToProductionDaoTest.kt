package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.getValue
import com.demo.developer.deraesw.demomoviewes.utils.DataTestUtils
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieToProductionDaoTest : BaseDaoTest() {

    private lateinit var movieToProductionDao: MovieToProductionDao
    private lateinit var movieDAO: MovieDAO
    lateinit var productionCompanyDao: ProductionCompanyDao

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    override fun initDb() {
        super.initDb()
        return runBlocking {
            movieToProductionDao = database.movieToProductionDao()

            productionCompanyDao = database.productionCompanyDao()
            movieDAO = database.movieDAO()

            movieDAO.bulkInsertMovies(DataTestUtils.movieList)
            productionCompanyDao.bulkForceInsert(DataTestUtils.productionList)
        }
    }


    @Test
    fun testBulkInsertTable() = runBlocking {
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        val list = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(
            list.size,
            equalTo(DataTestUtils.movieToProductionList.filter { it.idMovie == DataTestUtils.movie1.id }
                .count())
        )
    }

    @Test
    fun testDeleteCascadeMovie() = runBlocking {
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        val list = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size,
            equalTo(DataTestUtils.movieToProductionList.filter { it.idMovie == DataTestUtils.movie1.id }
                .count())
        )

        movieDAO.removeAllMovies()
        val listEmpty =
            getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(listEmpty.isEmpty(), equalTo(true))
    }

    @Test
    fun testDeleteAll() = runBlocking {
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        movieToProductionDao.removeAll()
        val listEmpty =
            getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(listEmpty.isEmpty(), equalTo(true))
    }

    @Test
    fun testBulkInsertDuplicate() = runBlocking {
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)
        movieToProductionDao.bulkForceInsert(DataTestUtils.movieToProductionList)

        val list = getValue(movieToProductionDao.selectProductionFromMovie(DataTestUtils.movie1.id))
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(
            list.size,
            equalTo(DataTestUtils.movieToProductionList.filter { it.idMovie == DataTestUtils.movie1.id }
                .count())
        )
    }
}