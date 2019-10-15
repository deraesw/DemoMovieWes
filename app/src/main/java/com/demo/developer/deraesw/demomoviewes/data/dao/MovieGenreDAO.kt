package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter

@Dao interface MovieGenreDAO {

    @Query("SELECT * FROM movie_genre ORDER BY name")
    fun selectAllMovieGenre() : LiveData<List<MovieGenre>>

    @Query("SELECT * FROM movie_genre ORDER BY name")
    fun selectAllMovieGenreForFilter() : LiveData<List<GenreFilter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bulkInsertMovieGenre(movieGenreList: List<MovieGenre>)

    @Query("DELETE FROM movie_genre")
    suspend fun removeAllData()
}