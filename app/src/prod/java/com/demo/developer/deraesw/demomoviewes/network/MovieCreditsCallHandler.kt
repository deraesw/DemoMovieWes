package com.demo.developer.deraesw.demomoviewes.network

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.network.response.MoviesResponse
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieCreditsCallHandler private constructor() : MainCallHandler() {

    private val TAG = MovieCreditsCallHandler::class.java.simpleName

    companion object {
        private var mInstance : MovieCreditsCallHandler? = null
        fun getInstance() : MovieCreditsCallHandler {
            mInstance ?: synchronized(this) {
                mInstance = MovieCreditsCallHandler()
            }
            return mInstance!!
        }
    }

    val mCreditsList : MutableLiveData<MovieCreditsListResponse> = MutableLiveData()

    fun fetchMovieCredits(idMovie : Int){
        val call = mMovieDbApi.fetchMovieCredit(idMovie, mApi)

        call.enqueue(object : Callback<MovieCreditsListResponse> {
            override fun onResponse(call: Call<MovieCreditsListResponse>, response: Response<MovieCreditsListResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        mCreditsList.postValue(response.body())
                    }
                } else {
                    if (response.errorBody() != null) {
                        //todo
                        Log.w(TAG, "Empty body")
                    }
                }
            }

            override fun onFailure(call: Call<MovieCreditsListResponse>, t: Throwable) {
                //todo
                Log.e(TAG, t.message, t);
            }
        })
    }
}