package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem

@Dao
interface CastingDAO {

    @Query("SELECT people.id, people.name, people.profilePath, casting.character " +
            "FROM casting " +
            "INNER JOIN people ON casting.peopleId = people.id " +
            "WHERE casting.movieId = :movieId ")
    fun selectCastingItemFromMovie(movieId : Int) : LiveData<List<CastingItem>>

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertCastings(list: List<Casting>)

    @Query("DELETE FROM casting WHERE movieId = :movieId ")
    fun deleteCastingFromMovie(movieId: Int)
}