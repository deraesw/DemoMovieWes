package com.demo.developer.deraesw.demomoviewes.data.dao

import com.demo.developer.deraesw.demomoviewes.data.entity.*

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

    val movieToGenreList = listOf(
            MovieToGenre(movie1.id, movieGenre1.id),
            MovieToGenre(movie1.id, movieGenre2.id),
            MovieToGenre(movie2.id, movieGenre2.id))

    val people1 = People(id = 1, name = "name_1", gender = 1)
    val people2 = People(id = 2, name = "name_2", gender = 2)
    val people2Duplicate = People(id = 2, name = "name_2_duplicate", gender = 1)
    val peopleList = listOf(people1, people2)


    val casting1 = Casting(id = 1, character =  "char_1", peopleId = people1.id, movieId = movie1.id, creditId = "credit_1")
    val casting2 = Casting(id = 2, character =  "char_2", peopleId = people2.id, movieId = movie1.id, creditId = "credit_2")
    val castingList = listOf(casting1, casting2)

    val production1 = ProductionCompany(id = 1, name = "production_1", originCountry = "US", logoPath = "logo")
    val production2 = ProductionCompany(id = 2, name = "production_2", originCountry = "US", logoPath = "logo")
    val production2Duplicate = ProductionCompany(id = 2, name = "production_2_duplicate", originCountry = "BE", logoPath = "logo")
    val production3 = ProductionCompany(id = 3, name = "production_3", originCountry = "US", logoPath = "logo")
    val productionList = listOf(production1, production2, production3)

    val movieToProductionList = listOf(
            MovieToProduction(movie1.id, production1.id),
            MovieToProduction(movie1.id, production2.id),
            MovieToProduction(movie2.id, production1.id),
            MovieToProduction(movie2.id, production3.id),
            MovieToProduction(movie3.id, production1.id)
    )
}