package com.demo.developer.deraesw.demomoviewes.core.data.dao
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.room.Room
//import android.database.sqlite.SQLiteConstraintException
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.platform.app.InstrumentationRegistry
//import com.demo.developer.deraesw.demomoviewes.data.appDatabase
//import kotlinx.coroutines.runBlocking
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class MovieToGenreDaoTest {
//
//    lateinit var database: appDatabase
//    lateinit var movieToGenreDAO: MovieToGenreDAO
//    lateinit var movieDAO: MovieDAO
//    lateinit var movieGenreDAO: MovieGenreDAO
//
//    @get:Rule
//    val testRule = InstantTaskExecutorRule()
//
//    @Before
//    fun initDb() {
//        return runBlocking {
//            database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, appDatabase::class.java)
//                    .build()
//
//            movieDAO = database.movieDAO()
//            movieGenreDAO = database.movieGenreDao()
//            movieDAO.bulkInsertMovies(DataTestUtils.movieList)
//            movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
//
//            movieToGenreDAO = database.movieToGenreDAO()
//        }
//    }
//
//    @After
//    fun closeDb(){
//        database.close()
//    }
//
//
//    @Test
//    fun insertListLinkListWithoutMovieNeitherGenre(){
//        try {
//            movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)
//            fail("SQLiteConstraintException expected")
//        } catch (ignored : SQLiteConstraintException) {}
//    }
//
//    @Test
//    fun insertListLinkWithoutMovie(){
//        try {
//            movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
//
//            movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)
//            fail("SQLiteConstraintException expected")
//        } catch (ignored : SQLiteConstraintException) {}
//    }
//
//    @Test
//    fun insertListLinkWithoutGenre(){
//        try {
//            movieDAO.bulkInsertMovies(DataTestUtils.movieList)
//
//            movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)
//            fail("SQLiteConstraintException expected")
//        } catch (ignored : SQLiteConstraintException) {}
//    }
//
//    @Test
//    fun insertListLink(){
//        movieGenreDAO.bulkInsertMovieGenre(DataTestUtils.movieGenreList)
//        movieDAO.bulkInsertMovies(DataTestUtils.movieList)
//
//        movieToGenreDAO.bulkInsertMovieToGenre(DataTestUtils.movieToGenreList)
//
//        val genreForMovie1 = LiveDataTestUtil.getValue(movieToGenreDAO.observeGenreListFromMovie(DataTestUtils.movie1.id))
//        assertTrue(genreForMovie1.isNotEmpty())
//        val genreForMovie2 = LiveDataTestUtil.getValue(movieToGenreDAO.observeGenreListFromMovie(DataTestUtils.movie2.id))
//        assertTrue(genreForMovie2.isNotEmpty())
//        val genreForMovie3 = LiveDataTestUtil.getValue(movieToGenreDAO.observeGenreListFromMovie(DataTestUtils.movie3.id))
//        assertTrue(genreForMovie3.isEmpty())
//    }
//
//    fun deleteMovie_checkIf_linkDeleted(){
//
//    }
//
//    fun deleteGenre_checkIf_linkDeleted(){
//
//    }
//}