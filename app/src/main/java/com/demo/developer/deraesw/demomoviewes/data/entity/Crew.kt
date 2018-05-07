package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "crew",
        indices = [
            (Index( value = ["peopleId", "movieId"] ))
        ],
        foreignKeys = [
            (ForeignKey(
                    entity = People::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("peopleId"),
                    onDelete = CASCADE)),
            (ForeignKey(
                    entity = Movie::class,
                    parentColumns = arrayOf("id" ),
                    childColumns = arrayOf("movieId"),
                    onDelete = CASCADE))
        ])
class Crew (
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var department : String = "",
    var job : String = "",
    var peopleId : Int = 0,
    var movieId : Int = 0)
{}