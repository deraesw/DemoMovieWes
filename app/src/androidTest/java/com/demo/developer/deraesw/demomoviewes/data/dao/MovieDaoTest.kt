package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.data.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var database: appDatabase
    private lateinit var movieDAO: MovieDAO

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        return runBlocking {
            database = Room
                .inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    appDatabase::class.java
                )
                .build()

            movieDAO = database.movieDAO()
        }
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testSelectOnEmptyTable() {
        val list = getValue(movieDAO.selectAllMovies())
        assertThat(list.isEmpty(), equalTo(true))
    }

    @Test
    fun testInsertSingleMovie() = runBlocking {
        movieDAO.insertMovie(DataTestUtils.movie1)

        val list = getValue(movieDAO.selectAllMovies())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(1))
        assertThat(list[0].title, equalTo(DataTestUtils.movie1.title))
    }

    @Test
    fun testBulkInsert() {
        movieDAO.bulkInsertMovies(DataTestUtils.movieList)

        val list = getValue(movieDAO.selectAllMovies())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieList.size))
        assertThat(list[0].title, equalTo(DataTestUtils.movieList[0].title))
        assertThat(list[1].title, equalTo(DataTestUtils.movieList[1].title))
        assertThat(list[2].title, equalTo(DataTestUtils.movieList[2].title))
    }

    @Test
    fun testInsertWithDuplicate() = runBlocking {
        movieDAO.insertMovie(DataTestUtils.movie2)
        val list = getValue(movieDAO.selectAllMovies())

        movieDAO.insertMovie(DataTestUtils.movie2Duplicate)
        val listDuplicate = getValue(movieDAO.selectAllMovies())
        assertThat(listDuplicate.isEmpty(), equalTo(false))
        assertThat(listDuplicate.size, equalTo(1))
        assertThat(listDuplicate[0].title, not(list[0].title))
    }

    @Test
    fun testBulkInsertWithDuplicate() {
        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
        val list = getValue(movieDAO.selectAllMovies())

        movieDAO.bulkInsertMovies(DataTestUtils.movieListWithDuplicate)
        val listDuplicate = getValue(movieDAO.selectAllMovies())
        assertThat(listDuplicate.isEmpty(), equalTo(false))
        assertThat(listDuplicate.size, equalTo(DataTestUtils.movieListWithDuplicate.size))
        assertThat(listDuplicate[0].title, equalTo(list[0].title))
        assertThat(listDuplicate[1].title, not(list[1].title))
        assertThat(listDuplicate[2].title, equalTo(list[2].title))
    }

    @Test
    fun testDeleteAllData() = runBlocking {
        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
        val list = getValue(movieDAO.selectAllMovies())
        assertThat(list.isEmpty(), equalTo(false))

        movieDAO.removeAllMovies()
        val listEmpty = getValue(movieDAO.selectAllMovies())
        assertThat(listEmpty.isEmpty(), equalTo(true))
    }

    @Test
    fun testDeleteObsoleteData() {
        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
        val list = getValue(movieDAO.selectAllMovies())
        assertThat(list.isEmpty(), equalTo(false))

        movieDAO.removeObsoleteMovies("10-01-2019")
        val listRemain = getValue(movieDAO.selectAllMovies())
        assertThat(listRemain.isEmpty(), equalTo(false))
        assertThat(listRemain.size, equalTo(list.size - 1))
        assertThat(listRemain[0].title, not(list[0].title))
    }

    @Test
    fun testSelectMoviesInTheater() {
        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
        val list = getValue(movieDAO.selectMoviesInTheater())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieList.filter { it.filterStatus == 1 }.count()))
    }

    @Test
    fun testSelectUpcomingMovies() {
        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
        val list = getValue(movieDAO.selectUpcomingMovies())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieList.filter { it.filterStatus == 2 }.count()))
    }
}