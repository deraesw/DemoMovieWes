package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.LiveDataTestUtil
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var database: appDatabase
    private lateinit var movieDAO: MovieDAO

    @Before
    fun initDb(){
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), appDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        movieDAO = database.movieDAO()
    }

    @After
    fun closeDb(){
        database.close()
    }

    @Test
    fun selectAllOnEmptyTable(){
        val list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertTrue(list.isEmpty())
    }

    @Test
    fun insertMovieAndCheckContent(){
        var list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertTrue(list.isEmpty())

        movieDAO.insertMovie(DataTestUtils.movie1)

        list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertFalse(list.isEmpty())
        assertTrue(list.size == 1)

        val movieInserted = LiveDataTestUtil.getValue(movieDAO.selectMovie(DataTestUtils.movie1.id))

        assertTrue(movieInserted.id == DataTestUtils.movie1.id)
        assertTrue(movieInserted.title == DataTestUtils.movie1.title)
        assertFalse(movieInserted.id == DataTestUtils.movie2.id)
    }

    @Test
    fun insertMovieWithDuplicateAndCheckContent(){
        var list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertTrue(list.isEmpty())

        movieDAO.insertMovie(DataTestUtils.movie2)
        list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertFalse(list.isEmpty())
        assertTrue(list.size == 1)

        val movieInserted = LiveDataTestUtil.getValue(movieDAO.selectMovie(DataTestUtils.movie2.id))
        assertTrue(movieInserted.id == DataTestUtils.movie2.id)
        assertTrue(movieInserted.title == DataTestUtils.movie2.title)

        movieDAO.insertMovie(DataTestUtils.movie2Duplicate)
        list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertFalse(list.isEmpty())
        assertTrue(list.size == 1)

        val movieInsertedDuplicate = LiveDataTestUtil.getValue(movieDAO.selectMovie(DataTestUtils.movie2.id))
        assertTrue(movieInsertedDuplicate.id == DataTestUtils.movie2Duplicate.id)
        assertTrue(movieInsertedDuplicate.title == DataTestUtils.movie2Duplicate.title)
        assertFalse(movieInserted == movieInsertedDuplicate)
    }

    @Test
    fun insertListMovieAndSelectAll(){
        var list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertTrue(list.isEmpty())

        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
        list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertFalse(list.isEmpty())
        assertTrue(list.size == DataTestUtils.movieList.size)
    }

    @Test
    fun insertListAndRemoveAll(){
        var list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertTrue(list.isEmpty())

        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
        list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertFalse(list.isEmpty())
        assertTrue(list.size == DataTestUtils.movieList.size)

        movieDAO.removeAllMovies()
        list = LiveDataTestUtil.getValue(movieDAO.selectAllMovies())
        assertTrue(list.isEmpty())
    }
}