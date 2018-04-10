package com.demo.developer.deraesw.demomoviewes.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "movie_genre")
class MovieGenre(
        @PrimaryKey var id: Int,
        var name: String) {
}