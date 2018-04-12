package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieToGenre

@Dao interface MovieToGenreDAO {

    @Query("SELECT * FROM movie_genre  INNER JOIN movie_to_genre ON movie_to_genre.idGenre = movie_genre.id  WHERE movie_to_genre.idMovie = :idMovie")
    fun selectGenreListFromMovie(idMovie : Int) : List<MovieGenre>

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun bulkInsertMovieToGenre(movieToGenre : List<MovieToGenre>)

    @Query("DELETE FROM movie_to_genre")
    fun deleteAllRow()
}