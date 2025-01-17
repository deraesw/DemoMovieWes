package com.demo.developer.deraesw.demomoviewes.core.network


import com.demo.developer.deraesw.demomoviewes.core.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.core.network.response.MovieGenreResponse
import com.demo.developer.deraesw.demomoviewes.core.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.core.network.response.MoviesResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviedbAPI {

    @GET("genre/movie/list")
    fun fetchMovieGenres(
        @Query("api_key") apiKey: String
    ): Call<MovieGenreResponse>

    @GET("movie/{id}")
    fun fetchMovieDetail(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") append: String = "credits"
    ): Call<MovieResponse>

    @GET("movie/now_playing")
    fun fetchNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("region") region: String
    ): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun fetchUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("region") region: String
    ): Call<MoviesResponse>

    @GET("movie/{id}/credits")
    fun fetchMovieCredit(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieCreditsListResponse>
}
