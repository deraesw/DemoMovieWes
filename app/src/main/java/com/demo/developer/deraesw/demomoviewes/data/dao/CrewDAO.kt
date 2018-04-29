package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Crew
import com.demo.developer.deraesw.demomoviewes.data.entity.People
import com.demo.developer.deraesw.demomoviewes.data.model.CrewItem

@Dao
interface CrewDAO {

    @Query("SELECT people.id, people.name, people.profilePath, crew.department ,crew.job " +
            "FROM crew " +
            "INNER JOIN people ON crew.peopleId = people.id " +
            "WHERE crew.movieId = :movieId ")
    fun selectCrewsItemFromMovie(movieId : Int) : LiveData<List<CrewItem>>

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertCrews(list: List<Crew>)

    @Query("DELETE FROM crew WHERE movieId = :movieId")
    fun deleteCrewFromMovie(movieId : Int)
}