package com.demo.developer.deraesw.demomoviewes.core.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.core.utils.DataTestUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest : BaseDaoTest() {

    private lateinit var movieDAO: MovieDAO

    @get:Rule
    val rule = InstantTaskExecutorRule()

    override fun initDb() {
        super.initDb()
        return runBlocking {
            movieDAO = database.movieDAO()
        }
    }

    @Test
    fun testSelectOnEmptyTable() = runBlocking {
        val list = movieDAO.selectAllMovies().first()
        assertThat(list.isEmpty(), equalTo(true))
    }

    @Test
    fun testInsertSingleMovie() = runBlocking {
        movieDAO.insert(DataTestUtils.movie1)

        val list = movieDAO.selectAllMovies().first()

        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(1))
        assertThat(list[0].title, equalTo(DataTestUtils.movie1.title))
    }

    @Test
    fun testBulkInsert() = runBlocking {
        movieDAO.bulkForceInsert(DataTestUtils.movieList)

        val list = movieDAO.selectAllMovies().first()
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieList.size))
        assertThat(list[0].title, equalTo(DataTestUtils.movieList[0].title))
        assertThat(list[1].title, equalTo(DataTestUtils.movieList[1].title))
        assertThat(list[2].title, equalTo(DataTestUtils.movieList[2].title))
    }

    @Test
    fun testInsertWithDuplicate() = runBlocking {
        movieDAO.insert(DataTestUtils.movie2)
        val list = movieDAO.selectAllMovies().first()

        movieDAO.insert(DataTestUtils.movie2Duplicate)
        val listDuplicate = movieDAO.selectAllMovies().first()
        assertThat(listDuplicate.isEmpty(), equalTo(false))
        assertThat(listDuplicate.size, equalTo(1))
        assertThat(listDuplicate[0].title, not(list[0].title))
    }

    @Test
    fun testBulkInsertWithDuplicate() = runBlocking {
        movieDAO.bulkForceInsert(DataTestUtils.movieList)
        val list = movieDAO.selectAllMovies().first()

        movieDAO.bulkForceInsert(DataTestUtils.movieListWithDuplicate)
        val listDuplicate = movieDAO.selectAllMovies().first()
        assertThat(listDuplicate.isEmpty(), equalTo(false))
        assertThat(listDuplicate.size, equalTo(DataTestUtils.movieListWithDuplicate.size))
        assertThat(listDuplicate[0].title, equalTo(list[0].title))
        assertThat(listDuplicate[1].title, not(list[1].title))
        assertThat(listDuplicate[2].title, equalTo(list[2].title))
    }

    @Test
    fun testDeleteAllData() = runBlocking {
        movieDAO.bulkForceInsert(DataTestUtils.movieList)
        val list = movieDAO.selectAllMovies().first()
        assertThat(list.isEmpty(), equalTo(false))

        movieDAO.removeAllMovies()
        val listEmpty = movieDAO.selectAllMovies().first()
        assertThat(listEmpty.isEmpty(), equalTo(true))
    }

    @Test
    fun testDeleteObsoleteData() = runBlocking {
        movieDAO.bulkForceInsert(DataTestUtils.movieList)
        val list = movieDAO.selectAllMovies().first()
        assertThat(list.isEmpty(), equalTo(false))

        movieDAO.removeObsoleteMovies("10-01-2019")
        val listRemain = movieDAO.selectAllMovies().first()
        assertThat(listRemain.isEmpty(), equalTo(false))
        assertThat(listRemain.size, equalTo(list.size - 1))
        assertThat(listRemain[0].title, not(list[0].title))
    }

    @Test
    fun testSelectMoviesInTheater() = runBlocking {
        movieDAO.bulkForceInsert(DataTestUtils.movieList)
        val list = movieDAO.selectMoviesInTheater().first()
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(
            list.size,
            equalTo(DataTestUtils.movieList.filter { it.filterStatus == 1 }.count())
        )
    }

    @Test
    fun testSelectUpcomingMovies() = runBlocking {
        movieDAO.bulkForceInsert(DataTestUtils.movieList)
        val list = movieDAO.selectUpcomingMovies().first()
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(
            list.size,
            equalTo(DataTestUtils.movieList.filter { it.filterStatus == 2 }.count())
        )
    }
}