package com.demo.developer.deraesw.demomoviewes.core.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.core.data.entity.People
import com.demo.developer.deraesw.demomoviewes.core.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
interface PeopleDAO : BaseDao<People> {

    @Query("SELECT * FROM people ORDER BY :orderBy")
    fun selectAllPeople(orderBy: String = "id"): LiveData<List<People>>

    @Query("DELETE FROM people")
    suspend fun deleteAll()

    @Query("DELETE FROM people WHERE insertDate <> :date")
    suspend fun removeObsoletePeople(date: String)

    suspend fun saveListPeople(list: List<People>) {
        withContext(Dispatchers.IO) {
            bulkForceInsert(list)
            removeObsoletePeople(DateUtils.getCurrentDate())
        }
    }
}