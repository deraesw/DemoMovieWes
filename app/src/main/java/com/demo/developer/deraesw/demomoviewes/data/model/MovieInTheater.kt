package com.demo.developer.deraesw.demomoviewes.data.model

class MovieInTheater (
        val id: Int,
        val title: String,
        val posterPath: String? = null,
        var runtime: Int = 0,
        var voteAverage: Float = 0.toFloat(),
        var releaseDate: String? = null){
}