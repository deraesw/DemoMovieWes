package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index

@Entity(tableName = "movie_to_genre",
        indices = [
            Index(value = ["idMovie", "idGenre"])
        ],
        primaryKeys = ["idMovie", "idGenre"],
        foreignKeys = [
            (ForeignKey(entity = Movie::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idMovie"))),
            (ForeignKey(entity = MovieGenre::class, parentColumns = arrayOf("id"), childColumns = arrayOf("idGenre")))])
class MovieToGenre (val idMovie : Int, val idGenre : Int) {
}