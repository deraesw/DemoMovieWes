package com.demo.developer.deraesw.demomoviewes.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(tableName = "movie_to_production",
        indices = [
            (Index(value = ["idMovie", "idProduction"]))
        ],
        primaryKeys = ["idMovie", "idProduction"],
        foreignKeys = [
            (ForeignKey(
                    entity = Movie::class,
                    parentColumns = ["id"],
                    childColumns = ["idMovie"],
                    onDelete = CASCADE)),
            (ForeignKey(
                    entity = ProductionCompany::class,
                    parentColumns = ["id"],
                    childColumns = ["idProduction"],
                    onDelete = CASCADE
            ))
        ]
)
class MovieToProduction(var idMovie : Int, var idProduction : Int)