package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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