package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index

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
class MovieToProduction(var idMovie : Int, var idProduction : Int) {
}