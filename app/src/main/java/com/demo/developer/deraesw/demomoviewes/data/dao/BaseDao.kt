package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj : T)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkForceInsert(list: List<T>)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun bulkIgnoreInsert(list: List<T>)
}