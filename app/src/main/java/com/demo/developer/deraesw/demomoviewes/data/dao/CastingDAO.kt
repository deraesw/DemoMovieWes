package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem

@Dao
interface CastingDAO {

    @Query("SELECT people.id, people.name, people.profilePath, casting.character " +
            "FROM casting " +
            "INNER JOIN people ON casting.peopleId = people.id " +
            "WHERE casting.movieId = :movieId " +
            "ORDER BY casting.position")
    fun selectCastingItemFromMovie(movieId : Int) : LiveData<List<CastingItem>>

    @Query("SELECT people.id, people.name, people.profilePath, casting.character " +
            "FROM casting " +
            "INNER JOIN people ON casting.peopleId = people.id " +
            "WHERE casting.movieId = :movieId " +
            "LIMIT :limit")
    fun selectLimitedCastingItemFromMovie(movieId : Int, limit: Int) : LiveData<List<CastingItem>>

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertCastings(list: List<Casting>)

    @Query("DELETE FROM casting WHERE movieId = :movieId ")
    fun deleteCastingFromMovie(movieId: Int)
}