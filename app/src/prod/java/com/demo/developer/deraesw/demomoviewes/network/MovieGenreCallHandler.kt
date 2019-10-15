package com.demo.developer.deraesw.demomoviewes.network

import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.network.response.MovieGenreResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieGenreCallHandler
@Inject constructor(){

    private val TAG = MovieGenreCallHandler::class.java.simpleName
    val mMovieGenreList : MutableLiveData<List<MovieGenre>> = MutableLiveData()

    @Inject
    lateinit var mMoviedbAPI: MoviedbAPI
    @Inject
    lateinit var mGson : Gson

    fun fetchGenreMovieList(){
        val call = mMoviedbAPI.fetchMovieGenres(BuildConfig.MOVIES_DB_API)

        call.enqueue(object : Callback<MovieGenreResponse> {
            override fun onResponse(call: Call<MovieGenreResponse>, response: Response<MovieGenreResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    //TODO remove if test successful
                    response.body()?.genres?.also {
                        mMovieGenreList.postValue(it)
                    }
//                        val list = response.body()?.genres
//                        if(list != null){
//                            mMovieGenreList.postValue(list)
//                        }

                } else {
                    if (response.errorBody() != null) {
                        //todo
                        Log.w(TAG, "Empty body")
                    }
                }
            }

            override fun onFailure(call: Call<MovieGenreResponse>, t: Throwable) {
                //todo
                Log.e(TAG, t.message, t)
            }
        })
    }

    fun fetchGenreMovieResponse() : Response<MovieGenreResponse> {
        return mMoviedbAPI.fetchMovieGenres(BuildConfig.MOVIES_DB_API).execute()
    }

    suspend fun getGenreMovieList() : List<MovieGenre> {
        return coroutineScope {
            val list = async {
                val response = mMoviedbAPI.fetchMovieGenres(BuildConfig.MOVIES_DB_API).execute()

                when {
                    response.isSuccessful && response.body() != null ->
                        return@async response.body()?.genres ?: emptyList()
                    response.errorBody() != null ->
                        throw NetworkException(mGson.fromJson(response.errorBody()?.string(), NetworkError::class.java))
                    else ->
                        return@async listOf<MovieGenre>()
                }
            }
            list.await()
        }
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