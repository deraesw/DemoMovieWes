package com.demo.developer.deraesw.demomoviewes.network

import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieGenreCallHandler
@Inject constructor(){

    @Inject
    lateinit var mMoviedbAPI: MoviedbAPI
    @Inject
    lateinit var mGson : Gson

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