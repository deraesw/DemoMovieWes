package com.demo.developer.deraesw.demomoviewes.network;


import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface moviedbAPI {

    @GET("genre/movie/list")
    Call<List<MovieGenre>> fetchMovieGenres(@Query("api_key") String apiKey);
}
