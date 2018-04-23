package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import com.demo.developer.deraesw.demomoviewes.data.entity.Crew
import com.demo.developer.deraesw.demomoviewes.data.entity.People

@Dao
interface CrewDAO {

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertCrews(list: List<Crew>)
}