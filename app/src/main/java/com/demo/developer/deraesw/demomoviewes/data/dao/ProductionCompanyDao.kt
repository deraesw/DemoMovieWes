package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.ProductionCompany
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

@Dao
interface ProductionCompanyDao : BaseDao<ProductionCompany> {

    @Query("SELECT * FROM production_company ORDER BY :orderBy")
    fun selectAll(orderBy: String = "id"): LiveData<List<ProductionCompany>>

    @Query("DELETE FROM production_company")
    suspend fun deleteAll()

    @Query("DELETE FROM production_company WHERE insertDate <> :date")
    suspend fun removeObsoleteProduction(date: String)

    suspend fun saveListProductionCompany(list: List<ProductionCompany>) {
        bulkForceInsert(list)
        removeObsoleteProduction(AppTools.getCurrentDate())
    }
}