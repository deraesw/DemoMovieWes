package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "production_company")
class ProductionCompany(
        @PrimaryKey
        var id : Int,
        @SerializedName("logo_path")
        var logoPath : String?,
        var name : String,
        @SerializedName("origin_country")
        var originCountry : String
)
