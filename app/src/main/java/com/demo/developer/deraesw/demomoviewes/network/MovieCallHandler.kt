package com.demo.developer.deraesw.demomoviewes.network

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieCallHandler private constructor(){

    private val TAG = MovieCallHandler::class.java.simpleName

    private val mRetrofit : Retrofit
    val mMovieList : MutableLiveData<List<Movie>> = MutableLiveData();

    init {
        val gson : Gson = GsonBuilder().setLenient().create();

        mRetrofit = Retrofit.Builder()
                .baseUrl(Constant.MOVIE_API_WEB)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    fun fetchNowPlayingMovies(){
        val call = mRetrofit.create(MoviedbAPI::class.java)
                                                    .fetchNowPlayingMovies(BuildConfig.MOVIES_DB_API);

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val list = response.body()?.results
                        if(list != null){
                            mMovieList.postValue(list)
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        //todo
                        Log.w(TAG, "Empty body")
                    }
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                //todo
                Log.e(TAG, t.message, t);
            }
        })
    }

    companion object {

        private var mInstance : MovieCallHandler? = null

        fun getInstance() : MovieCallHandler {
            mInstance ?: synchronized(this) {
                mInstance = MovieCallHandler()
            }
            return mInstance!!
        }
    }
}