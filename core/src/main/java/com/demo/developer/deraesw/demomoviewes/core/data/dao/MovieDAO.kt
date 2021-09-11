package com.demo.developer.deraesw.demomoviewes.core.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.core.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.core.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.core.data.model.UpcomingMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO : BaseDao<Movie> {

    @Query("SELECT * from movie WHERE id = :movieId ")
    fun selectMovie(movieId: Int): Flow<Movie>

    @Query("SELECT * FROM movie")
    fun selectAllMovies(): Flow<List<Movie>>

    @Query("SELECT id, title, posterPath, runtime, voteAverage, releaseDate FROM movie WHERE filterStatus = 1")
    fun selectMoviesInTheater(): Flow<List<MovieInTheater>>

    @Query("SELECT id, title, posterPath, runtime, releaseDate FROM movie WHERE filterStatus = 2")
    fun selectUpcomingMovies(): Flow<List<UpcomingMovie>>

    @Query("DELETE FROM movie")
    suspend fun removeAllMovies()

    @Query("DELETE FROM movie WHERE insertDate <> :date")
    suspend fun removeObsoleteMovies(date: String)

}