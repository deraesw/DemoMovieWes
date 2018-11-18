package com.demo.developer.deraesw.demomoviewes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_genre")
class MovieGenre(
        @PrimaryKey var id: Int,
        var name: String)