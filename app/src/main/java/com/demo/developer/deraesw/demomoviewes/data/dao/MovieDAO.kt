package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater

@Dao
interface MovieDAO {

    @Query("SELECT * from movie WHERE id = :movieId ")
    fun selectMovie(movieId : Int) : LiveData<Movie>

    @Query("SELECT * FROM movie")
    fun selectAllMovies() : LiveData<List<Movie>>

    @Query("SELECT id, title, posterPath, runtime, voteAverage, releaseDate FROM movie")
    fun selectMoviesInTheater() : LiveData<List<MovieInTheater>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertMovies(movieList: List<Movie>)

    @Query("DELETE FROM movie")
    fun removeAllMovies()

}