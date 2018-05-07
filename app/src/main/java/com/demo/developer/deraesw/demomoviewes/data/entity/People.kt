package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "people")
class People(
    @PrimaryKey
    var id : Int = 0,
    var name : String = "",
    var gender : Int = 0,
    var profilePath : String  = "") {

}