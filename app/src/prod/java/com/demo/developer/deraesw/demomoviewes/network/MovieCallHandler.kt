package com.demo.developer.deraesw.demomoviewes.network

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.network.response.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCallHandler
@Inject constructor(){

    companion object {
        private var mInstance : MovieCallHandler? = null
        fun getInstance() : MovieCallHandler {
            mInstance ?: synchronized(this) {
                mInstance = MovieCallHandler()
            }
            return mInstance!!
        }
    }

    private val TAG = MovieCallHandler::class.java.simpleName
    private val mApi = BuildConfig.MOVIES_DB_API

    val mMovieList : MutableLiveData<List<Movie>> = MutableLiveData()
    val mMovie : MutableLiveData<Movie> = MutableLiveData()

    @Inject
    lateinit var mMovieDbApi: MoviedbAPI


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
}