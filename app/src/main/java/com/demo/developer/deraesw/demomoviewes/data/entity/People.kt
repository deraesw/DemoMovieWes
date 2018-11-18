package com.demo.developer.deraesw.demomoviewes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
class People(
    @PrimaryKey
    var id : Int = 0,
    var name : String = "",
    var gender : Int = 0,
    var profilePath : String  = "")