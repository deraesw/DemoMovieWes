package com.demo.developer.deraesw.demomoviewes.network

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.network.response.MovieGenreResponse
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieGenreCallHandler
@Inject constructor(){

    private val TAG = MovieGenreCallHandler::class.java.simpleName
    val mMovieGenreList : MutableLiveData<List<MovieGenre>> = MutableLiveData();

    @Inject
    lateinit var mMoviedbAPI: MoviedbAPI

    fun fetchGenreMovieList(){
        val call = mMoviedbAPI.fetchMovieGenres(BuildConfig.MOVIES_DB_API);

        call.enqueue(object : Callback<MovieGenreResponse> {
            override fun onResponse(call: Call<MovieGenreResponse>, response: Response<MovieGenreResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val list = response.body()?.genres
                        if(list != null){
                            mMovieGenreList.postValue(list)
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        //todo
                        Log.w(TAG, "Empty body")
                    }
                }
            }

            override fun onFailure(call: Call<MovieGenreResponse>, t: Throwable) {
                //todo
                Log.e(TAG, t.message, t);
            }
        })
    }

    companion object {

        private var mInstance : MovieGenreCallHandler? = null

        fun getInstance() : MovieGenreCallHandler {
            mInstance ?: synchronized(this) {
                mInstance = MovieGenreCallHandler()
            }
            return mInstance!!
        }
    }
}