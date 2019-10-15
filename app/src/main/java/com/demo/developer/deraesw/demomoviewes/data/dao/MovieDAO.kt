package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.data.model.UpcomingMovie
import com.demo.developer.deraesw.demomoviewes.utils.Constant

@Dao
interface MovieDAO {

    @Query("SELECT * from movie WHERE id = :movieId ")
    fun selectMovie(movieId : Int) : LiveData<Movie>

    @Query("SELECT * FROM movie")
    fun selectAllMovies() : LiveData<List<Movie>>

    @Query("SELECT id, title, posterPath, runtime, voteAverage, releaseDate FROM movie WHERE filterStatus = 1")
    fun selectMoviesInTheater() : LiveData<List<MovieInTheater>>

    @Query("SELECT id, title, posterPath, runtime, releaseDate FROM movie WHERE filterStatus = 2")
    fun selectUpcomingMovies() : LiveData<List<UpcomingMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertMovies(movieList: List<Movie>)

    @Query("DELETE FROM movie")
    suspend fun removeAllMovies()

    @Query("DELETE FROM movie WHERE insertDate <> :date")
    fun removeObsoleteMovies(date: String)

}