package com.demo.developer.deraesw.demomoviewes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "people")
data class People(
    @PrimaryKey
    var id : Int = 0,
    var name : String = "",
    var gender : Int = 0,
    var profilePath : String  = "",
    @SerializedName(value = "insert_date")
    var insertDate: String= "")