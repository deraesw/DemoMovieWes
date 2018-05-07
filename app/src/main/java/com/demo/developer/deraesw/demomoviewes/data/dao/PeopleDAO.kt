package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.People

@Dao
interface PeopleDAO {

    @Query("SELECT * FROM people ORDER BY :orderBy")
    fun selectAllPeople(orderBy : String = "id") : LiveData<List<People>>

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertPeoples(list: List<People>)

    @Query("DELETE FROM people")
    fun deleteAll()
}