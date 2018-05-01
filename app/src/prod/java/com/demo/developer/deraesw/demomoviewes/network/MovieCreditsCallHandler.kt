package com.demo.developer.deraesw.demomoviewes.network

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCreditsCallHandler
@Inject constructor() {

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

    @Inject
    lateinit var mMovieDbApi: MoviedbAPI

    fun fetchMovieCredits(idMovie : Int){
        val call = mMovieDbApi.fetchMovieCredit(idMovie, BuildConfig.MOVIES_DB_API)

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