package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.People

@Dao
interface PeopleDAO : BaseDao<People> {

    @Query("SELECT * FROM people ORDER BY :orderBy")
    fun selectAllPeople(orderBy: String = "id"): LiveData<List<People>>

    @Query("DELETE FROM people")
    suspend fun deleteAll()

    @Query("DELETE FROM people WHERE insertDate <> :date")
    suspend fun removeObsoletePeople(date: String)
}