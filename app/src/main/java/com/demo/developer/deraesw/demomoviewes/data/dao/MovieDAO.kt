package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater

@Dao
interface MovieDAO {

    @Query("SELECT * FROM movie")
    fun selectAllMovies() : LiveData<List<Movie>>

    @Query("SELECT * FROM movie")
    fun selectMoviesInTheater() : LiveData<List<MovieInTheater>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertMovies(movieList: List<Movie>)

    @Query("DELETE FROM movie")
    fun removeAllMovies()

}