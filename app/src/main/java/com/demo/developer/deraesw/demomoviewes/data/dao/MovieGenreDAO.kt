package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieGenreDAO : BaseDao<MovieGenre> {

    @Query("SELECT * FROM movie_genre ORDER BY name")
    fun selectAllMovieGenre(): Flow<List<MovieGenre>>

    @Query("SELECT * FROM movie_genre ORDER BY name")
    fun selectAllMovieGenreForFilter(): LiveData<List<GenreFilter>>

    @Query("DELETE FROM movie_genre")
    suspend fun removeAllData()
}