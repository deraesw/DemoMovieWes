package com.demo.developer.deraesw.demomoviewes.core.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bulkForceInsert(list: List<T>)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun bulkIgnoreInsert(list: List<T>)
}