package com.demo.developer.deraesw.demomoviewes.core.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "casting",
    indices = [
        (Index(value = ["peopleId", "movieId"]))
    ],
    foreignKeys = [
        (ForeignKey(
            entity = People::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("peopleId"),
            onDelete = CASCADE
        )),
        (ForeignKey(
            entity = Movie::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movieId"),
            onDelete = CASCADE
        ))
    ]
)
class Casting(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var character: String = "",
    var peopleId: Int = 0,
    var movieId: Int = 0,
    var creditId: String = "",
    var position: Int = 0,
    @SerializedName(value = "insert_date")
    var insertDate: String = ""
)