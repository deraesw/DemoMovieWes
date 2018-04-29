package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import android.support.test.runner.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieGenreDAOTest {

    private lateinit var database: appDatabase

    @Before fun initDb(){
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                appDatabase::class.java
        ).build()
    }

    @After
    fun closeDb(){
        database.close()
    }

    @Test fun insertListGenreAndFetchAllGenre(){
        database.movieGenreDao().bulkInsertMovieGenre(getGenreList())

        val allGenre = database.movieGenreDao().selectAllMovieGenreList()

        assertTrue(allGenre.size == getGenreList().size)
    }

    @Test fun insertListGenreDuplicateAndFetchAllGenre(){
        val item = listOf(genre1)
        val duplicateItem = listOf(genre1DuplicateId)

        database.movieGenreDao().bulkInsertMovieGenre(item)
        val allGenre = database.movieGenreDao().selectAllMovieGenreList()

        assertTrue(allGenre.size == item.size)
        assertTrue(allGenre[0].name == genre1.name)

        database.movieGenreDao().bulkInsertMovieGenre(duplicateItem)
        val allGenreWithDuplicate = database.movieGenreDao().selectAllMovieGenreList()

        assertTrue("Both list as same size", allGenreWithDuplicate.size == allGenre.size)
        assertTrue("Duplicate item is well inserted",allGenreWithDuplicate[0].name == genre1DuplicateId.name)
        assertFalse("Duplicated item is different of item",allGenreWithDuplicate[0].name == allGenre[0].name)

    }

    companion object {
        val genre1 = MovieGenre(1, "action")
        val genre1DuplicateId = MovieGenre(1, "action_bis")
        val genre2 = MovieGenre(2, "comedy")
        fun getGenreList() : List<MovieGenre>{
            return listOf(genre1, genre2)
        }
    }
}