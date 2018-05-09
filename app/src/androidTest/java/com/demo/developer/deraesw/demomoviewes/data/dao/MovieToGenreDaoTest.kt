package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.LiveDataTestUtil
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieToGenreDaoTest {

    @JvmField
    @Rule
    val testRule = InstantTaskExecutorRule()

    lateinit var database: appDatabase
    lateinit var movieToGenreDAO: MovieToGenreDAO
    lateinit var movieDAO: MovieDAO
    lateinit var movieGenreDAO: MovieGenreDAO

    @Before
    fun initDb(){
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), appDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        movieToGenreDAO = database.movieToGenreDAO()
        movieDAO = database.movieDAO()
        movieGenreDAO = database.movieGenreDao()
    }

    @After
    fun closeDb(){
        database.close()
    }


    @Test
    fun insertListLinkListWithoutMovieNeitherGenre(){
        try {
            movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)
            fail("SQLiteConstraintException expected")
        } catch (ignored : SQLiteConstraintException) {}
    }

    @Test
    fun insertListLinkWithoutMovie(){
        try {
            movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)

            movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)
            fail("SQLiteConstraintException expected")
        } catch (ignored : SQLiteConstraintException) {}
    }

    @Test
    fun insertListLinkWithoutGenre(){
        try {
            movieDAO.bulkInsertMovies(DataTestUtils.movieList)

            movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)
            fail("SQLiteConstraintException expected")
        } catch (ignored : SQLiteConstraintException) {}
    }

    @Test
    fun insertListLink(){
        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
        movieDAO.bulkInsertMovies(DataTestUtils.movieList)

        movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)

        val genreForMovie1 = LiveDataTestUtil.getValue(movieToGenreDAO.observeGenreListFromMovie(DataTestUtils.movie1.id))
        assertTrue(genreForMovie1.isNotEmpty())
        val genreForMovie2 = LiveDataTestUtil.getValue(movieToGenreDAO.observeGenreListFromMovie(DataTestUtils.movie2.id))
        assertTrue(genreForMovie2.isNotEmpty())
        val genreForMovie3 = LiveDataTestUtil.getValue(movieToGenreDAO.observeGenreListFromMovie(DataTestUtils.movie3.id))
        assertTrue(genreForMovie3.isEmpty())
    }

    fun deleteMovie_checkIf_linkDeleted(){

    }

    fun deleteGenre_checkIf_linkDeleted(){

    }
}