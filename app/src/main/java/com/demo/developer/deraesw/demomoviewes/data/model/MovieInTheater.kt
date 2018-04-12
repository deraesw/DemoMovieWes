package com.demo.developer.deraesw.demomoviewes.data.model

import android.arch.persistence.room.Ignore
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre

class MovieInTheater (
        val id: Int,
        val title: String,
        val posterPath: String? = null,
        var runtime: Int = 0,
        var voteAverage: Float = 0.toFloat(),
        var releaseDate: String? = null){
    @Ignore var genres : List<MovieGenre> = ArrayList()
}