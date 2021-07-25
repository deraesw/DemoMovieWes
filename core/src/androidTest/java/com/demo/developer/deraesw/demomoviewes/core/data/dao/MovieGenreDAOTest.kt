package com.demo.developer.deraesw.demomoviewes.core.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.core.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.core.utils.DataTestUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieGenreDAOTest : BaseDaoTest() {

    private lateinit var movieGenreDAO: MovieGenreDAO

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    override fun initDb() {
        super.initDb()
        return runBlocking {
            movieGenreDAO = database.movieGenreDao()
        }
    }

    @Test
    fun selectEmptyTable() = runBlocking {
        val list: List<MovieGenre> = movieGenreDAO.selectAllMovieGenre().first()

        assertThat(list.size, equalTo(0))
    }

    @Test
    fun insertListGenre() = runBlocking {
        movieGenreDAO.bulkForceInsert(DataTestUtils.movieGenreList)

        val list: List<MovieGenre> = movieGenreDAO.selectAllMovieGenre().first()

        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))
    }

    @Test
    fun insertListGenreWithDuplicateItem() = runBlocking {
        movieGenreDAO.bulkForceInsert(DataTestUtils.movieGenreList)
        var list: List<MovieGenre> = movieGenreDAO.selectAllMovieGenre().first()
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))

        val duplicateItem = listOf(DataTestUtils.movieGenre2Duplicate)
        movieGenreDAO.bulkForceInsert(duplicateItem)
        list = movieGenreDAO.selectAllMovieGenre().first()
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))
    }

    @Test
    fun insertListGenreAndRemoveAll() = runBlocking {
        movieGenreDAO.bulkForceInsert(DataTestUtils.movieGenreList)
        var list: List<MovieGenre> = movieGenreDAO.selectAllMovieGenre().first()
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))

        movieGenreDAO.removeAllData()
        list = movieGenreDAO.selectAllMovieGenre().first()
        assertThat(list.size, equalTo(0))
    }
}