package com.demo.developer.deraesw.demomoviewes.network

import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieGenreCallHandler {

    val mRetrofit : Retrofit

    init {
        val gson : Gson = GsonBuilder().setLenient().create();

        mRetrofit = Retrofit.Builder()
                .baseUrl(Constant.MOVIE_API_WEB)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    fun fetchGenreMovieList(){

        val call = mRetrofit.create(moviedbAPI::class.java)
                                                    .fetchMovieGenres(BuildConfig.MOVIES_DB_API);

        call.enqueue(object : Callback<List<MovieGenre>> {
            override fun onResponse(call: Call<List<MovieGenre>>, response: Response<List<MovieGenre>>) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        val list = response.body()
                        //todo
                    }
                } else {
                    if (response.errorBody() != null) {
                        //todo
                    }
                }
            }

            override fun onFailure(call: Call<List<MovieGenre>>, t: Throwable) {
                //todo
            }
        })

    }

}