package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertThat

@RunWith(AndroidJUnit4::class)
class MovieGenreDAOTest {

    private lateinit var database: appDatabase
    private lateinit var movieGenreDAO: MovieGenreDAO

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        return runBlocking {
            database = Room.inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    appDatabase::class.java)
                    .build()

            movieGenreDAO = database.movieGenreDao()
        }
    }

    @After
    fun closeDb(){
        database.close()
    }

    @Test
    fun selectEmptyTable(){
        val list : List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())

        assertThat(list.size, equalTo(0))
    }

    @Test
    fun insertListGenre(){
        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)

        val list : List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())

        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))
    }

    @Test
    fun insertListGenreWithDuplicateItem(){
        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
        var list : List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.isEmpty(), equalTo(false))
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))

        val duplicateItem = listOf(DataTestUtils.movieGenre2Duplicate)
        movieGenreDAO.bulkInsertMovieGenre(duplicateItem)
        list = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))
    }

    @Test
    fun insertListGenreAndRemoveAll(){
        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
        var list : List<MovieGenre> = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.size, equalTo(DataTestUtils.movieGenreList.size))

        movieGenreDAO.removeAllData()
        list = getValue(movieGenreDAO.selectAllMovieGenre())
        assertThat(list.size, equalTo(0))
    }
}