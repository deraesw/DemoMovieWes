package com.demo.developer.deraesw.demomoviewes.data.dao

import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre

object DataTestUtils {

    val movieGenre1 = MovieGenre(1, "genre_1")
    val movieGenre2 = MovieGenre(2, "genre_2")
    val movieGenre2Duplicate = MovieGenre(2, "genre_2_duplicate")
    val movieGenre3 = MovieGenre(3, "genre_3")

    val movieGenreList = listOf(movieGenre1, movieGenre2, movieGenre3)

    val movie1 = Movie(id = 1, title = "movie_1")
    val movie2 = Movie(id = 2, title = "movie_2")
    val movie2Duplicate = Movie(id = 2, title = "movie_2_duplicate")
    val movie3 = Movie(id = 3, title = "movie_3")

    val movieList = listOf(movie1, movie2, movie3)
}