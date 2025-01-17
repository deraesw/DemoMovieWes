package com.demo.developer.deraesw.demomoviewes.network


import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.network.response.MovieGenreResponse
import com.demo.developer.deraesw.demomoviewes.network.response.MoviesResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviedbAPI {

    @GET("genre/movie/list")
    fun fetchMovieGenres(@Query("api_key") apiKey: String)
            : Call<MovieGenreResponse>

    @GET("movie/{id}")
    fun fetchMovieDetail(@Path("id") movieId: Int, @Query("api_key") apiKey: String)
            : Call<Movie>

    @GET("movie/now_playing")
    fun fetchNowPlayingMovies(@Query("api_key") apiKey: String)
            : Call<MoviesResponse>
}
