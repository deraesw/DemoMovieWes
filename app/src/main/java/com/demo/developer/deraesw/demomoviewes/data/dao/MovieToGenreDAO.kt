package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieToGenre
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieToGenreDAO : BaseDao<MovieToGenre> {

    //TODO: Look to normalize naming

    @Query("SELECT * FROM movie_genre  INNER JOIN movie_to_genre ON movie_to_genre.idGenre = movie_genre.id  WHERE movie_to_genre.idMovie = :idMovie")
    fun selectGenreListFromMovie(idMovie: Int): List<MovieGenre>

    @Query("SELECT * FROM movie_genre  INNER JOIN movie_to_genre ON movie_to_genre.idGenre = movie_genre.id  WHERE movie_to_genre.idMovie = :idMovie")
    fun observeGenreListFromMovie(idMovie: Int): Flow<List<MovieGenre>>

    @Query("DELETE FROM movie_to_genre")
    suspend fun deleteAllRow()
}