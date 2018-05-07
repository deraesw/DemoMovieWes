package com.demo.developer.deraesw.demomoviewes.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.ProductionCompany

@Dao
interface ProductionCompanyDao : BaseDao<ProductionCompany> {

    @Query("SELECT * FROM production_company ORDER BY :orderBy")
    fun selectAll(orderBy : String = "id") : LiveData<List<ProductionCompany>>

    @Query("DELETE FROM production_company")
    fun deleteAll()
}