package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieToProduction
import com.demo.developer.deraesw.demomoviewes.data.entity.ProductionCompany

@Dao
interface MovieToProductionDao : BaseDao<MovieToProduction> {

    @Query("SELECT production_company.* " +
            "FROM movie_to_production " +
            "INNER JOIN production_company ON movie_to_production.idProduction = production_company.id " +
            "WHERE movie_to_production.idMovie = :movieId")
    fun selectProductionFromMovie(movieId : Int) : LiveData<List<ProductionCompany>>

    @Query("DELETE FROM movie_to_production")
    suspend fun removeAll()
}