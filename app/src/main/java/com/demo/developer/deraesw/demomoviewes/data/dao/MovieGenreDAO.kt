package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter

@Dao interface MovieGenreDAO {

    @Query("SELECT * FROM movie_genre ORDER BY name")
    fun selectAllMovieGenre() : LiveData<List<MovieGenre>>

    @Query("SELECT * FROM movie_genre")
    fun selectAllMovieGenreList() : List<MovieGenre>

    @Query("SELECT * FROM movie_genre ORDER BY name")
    fun selectAllMovieGenreForFilter() : LiveData<List<GenreFilter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertMovieGenre(movieGenreList: List<MovieGenre>)

    @Query("DELETE FROM movie_genre")
    fun removeAllData()
}