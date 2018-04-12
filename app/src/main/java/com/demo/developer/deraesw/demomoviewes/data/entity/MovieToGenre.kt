package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(tableName = "movie_to_genre",
        primaryKeys = ["idMovie", "idGenre"],
        foreignKeys = [
            (ForeignKey(entity = Movie::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idMovie"))),
            (ForeignKey(entity = MovieGenre::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idGenre")))])
class MovieToGenre (val idMovie : Int, val idGenre : Int) {
}