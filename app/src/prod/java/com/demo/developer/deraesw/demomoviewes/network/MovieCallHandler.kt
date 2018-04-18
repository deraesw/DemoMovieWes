package com.demo.developer.deraesw.demomoviewes.network

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.network.response.MoviesResponse
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

    private val mMovieDbApi : MoviedbAPI
    private val mApi = BuildConfig.MOVIES_DB_API

    val mMovieList : MutableLiveData<List<Movie>> = MutableLiveData()
    val mMovie : MutableLiveData<Movie> = MutableLiveData()

    init {
        val gson : Gson = GsonBuilder().setLenient().create();

        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.MOVIE_API_WEB)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        mMovieDbApi = retrofit.create(MoviedbAPI::class.java)
    }

    fun fetchMovieDetail(id : Int){
        val call = mMovieDbApi.fetchMovieDetail(id, mApi)

        call.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                Log.e(TAG, t?.message, t)
            }

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if(response.isSuccessful) {
                    val movie = response.body()
                    if(movie != null){
                        mMovie.postValue(movie)
                    }
                }
            }
        })
    }

    fun fetchNowPlayingMovies(){
        val call = mMovieDbApi.fetchNowPlayingMovies(mApi)

        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val list = response.body()?.results
                        if(list != null){
                            handleFetchingMovieDetailFromList(list)
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        //todo
                        Log.w(TAG, "Empty body")
                    }
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                //todo
                Log.e(TAG, t.message, t);
            }
        })
    }

    private fun handleFetchingMovieDetailFromList(movieList: List<Movie>) {
        var completeMovieList : List<Movie> = ArrayList()

        val handler = Handler()
        movieList.forEach({
            handler.postDelayed({
                val call = mMovieDbApi.fetchMovieDetail(it.id, mApi)

                call.enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                        Log.e(TAG, t?.message, t)
                    }

                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        if(response.isSuccessful) {
                            val movie = response.body()
                            if(movie != null){
                                completeMovieList += movie
                                if(movieList.size == completeMovieList.size){
                                    mMovieList.postValue(completeMovieList)
                                }
                            }
                        }
                    }
                })
            }, 500)
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