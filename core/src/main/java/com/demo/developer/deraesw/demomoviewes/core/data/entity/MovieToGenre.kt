package com.demo.developer.deraesw.demomoviewes.core.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
    tableName = "movie_to_genre",
    indices = [
        Index(value = ["idMovie", "idGenre"])
    ],
    primaryKeys = ["idMovie", "idGenre"],
    foreignKeys = [
        (ForeignKey(
            entity = Movie::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idMovie"),
            onDelete = CASCADE
        )),
        (ForeignKey(
            entity = MovieGenre::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idGenre"),
            onDelete = CASCADE
        ))]
)
class MovieToGenre(val idMovie: Int, val idGenre: Int)