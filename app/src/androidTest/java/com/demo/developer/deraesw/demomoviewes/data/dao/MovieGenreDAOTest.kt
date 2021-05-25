package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.getValue
import com.demo.developer.deraesw.demomoviewes.utils.DataTestUtils
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
    fun selectEmptyTable() {
        val list: List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())

        assertThat(list.size, equalTo(0))
    }

    @Test
    fun insertListGenre() = runBlocking {
        movieGenreDAO.bulkForceInsert(DataTestUtils.movieGenreList)

        val list: List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())

        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))
    }

    @Test
    fun insertListGenreWithDuplicateItem() = runBlocking {
        movieGenreDAO.bulkForceInsert(DataTestUtils.movieGenreList)
        var list: List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))

        val duplicateItem = listOf(DataTestUtils.movieGenre2Duplicate)
        movieGenreDAO.bulkForceInsert(duplicateItem)
        list = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))
    }

    @Test
    fun insertListGenreAndRemoveAll() = runBlocking {
        movieGenreDAO.bulkForceInsert(DataTestUtils.movieGenreList)
        var list: List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))

        movieGenreDAO.removeAllData()
        list = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.size, equalTo(0))
    }
}