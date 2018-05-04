package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.LiveDataTestUtil
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieGenreDAOTest {

    @JvmField
    @Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var database: appDatabase
    private lateinit var movieGenreDAO: MovieGenreDAO

    @Before
    fun initDb(){
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), appDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        movieGenreDAO = database.movieGenreDao()
    }

    @After
    fun closeDb(){
        database.close()
    }

    @Test
    fun selectEmptyTable(){
        val list : List<MovieGenre> = LiveDataTestUtil.getValue(movieGenreDAO.selectAllMovieGenre())

        assertTrue(list.isEmpty())
    }

    @Test
    fun insertListGenre(){
        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)

        val list : List<MovieGenre> = LiveDataTestUtil.getValue(movieGenreDAO.selectAllMovieGenre())

        assertFalse(list.isEmpty())
        assertTrue(list.size == DataTestUtils.movieGenreList.size)
    }

    @Test
    fun insertListGenreWithDuplicateItem(){
        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
        var list : List<MovieGenre> = LiveDataTestUtil.getValue(movieGenreDAO.selectAllMovieGenre())
        assertFalse(list.isEmpty())
        assertTrue(list.size == DataTestUtils.movieGenreList.size)

        val duplicateItem = listOf(DataTestUtils.movieGenre2Duplicate)
        movieGenreDAO.bulkInsertMovieGenre(duplicateItem)
        list = LiveDataTestUtil.getValue(movieGenreDAO.selectAllMovieGenre())
        assertFalse(list.isEmpty())
        assertTrue(list.size == DataTestUtils.movieGenreList.size)
    }

    @Test
    fun insertListGenreAndRemoveAll(){
        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
        var list : List<MovieGenre> = LiveDataTestUtil.getValue(movieGenreDAO.selectAllMovieGenre())
        assertFalse(list.isEmpty())
        assertTrue(list.size == DataTestUtils.movieGenreList.size)

        movieGenreDAO.removeAllData()
        list = LiveDataTestUtil.getValue(movieGenreDAO.selectAllMovieGenre())
        assertTrue(list.isEmpty())
    }
}