package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "casting",
        foreignKeys = [
            (ForeignKey(entity = People::class, parentColumns = arrayOf("id"), childColumns = arrayOf("peopleId"))),
            (ForeignKey(entity = Movie::class, parentColumns = arrayOf("id"), childColumns = arrayOf("movieId")))
        ])
class Casting {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    var character : String = ""
    var peopleId : Int = 0
    var movieId : Int = 0
    var creditId : String = ""
}