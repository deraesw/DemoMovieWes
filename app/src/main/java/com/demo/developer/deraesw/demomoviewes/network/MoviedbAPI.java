package com.demo.developer.deraesw.demomoviewes.network;


import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre;
import com.demo.developer.deraesw.demomoviewes.network.response.MovieGenreResponse;
import com.demo.developer.deraesw.demomoviewes.network.response.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviedbAPI {

    @GET("genre/movie/list")
    Call<MovieGenreResponse> fetchMovieGenres(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<MovieResponse> fetchNowPlayingMovies(@Query("api_key") String apiKey);
}
