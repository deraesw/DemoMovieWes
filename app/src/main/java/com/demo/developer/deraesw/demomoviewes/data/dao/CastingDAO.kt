package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem

@Dao
interface CastingDAO : BaseDao<Casting> {

    @Query(
        "SELECT people.id, people.name, people.profilePath, casting.character " +
                "FROM casting " +
                "INNER JOIN people ON casting.peopleId = people.id " +
                "WHERE casting.movieId = :movieId " +
                "ORDER BY casting.position"
    )
    fun selectCastingItemFromMovie(movieId: Int): LiveData<List<CastingItem>>

    @Query(
        "SELECT people.id, people.name, people.profilePath, casting.character " +
                "FROM casting " +
            "INNER JOIN people ON casting.peopleId = people.id " +
            "WHERE casting.movieId = :movieId " +
            "LIMIT :limit")
    fun selectLimitedCastingItemFromMovie(movieId : Int, limit: Int) : LiveData<List<CastingItem>>

    @Query("DELETE FROM casting WHERE movieId = :movieId ")
    suspend fun deleteCastingFromMovie(movieId: Int)

    @Query("DELETE FROM casting WHERE insertDate <> :date")
    suspend fun removeObsoleteCasting(date: String)
}