package com.demo.developer.deraesw.demomoviewes.network

import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class MainCallHandler () {
    protected val mMovieDbApi : MoviedbAPI
    protected val mApi = BuildConfig.MOVIES_DB_API

    init {
        val gson : Gson = GsonBuilder().setLenient().create();

        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.MOVIE_API_WEB)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        mMovieDbApi = retrofit.create(MoviedbAPI::class.java)
    }
}