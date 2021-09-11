package com.demo.developer.deraesw.demomoviewes.core.network.handler

import com.demo.developer.deraesw.demomoviewes.core.BuildConfig
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.core.network.MoviedbAPI
import com.demo.developer.deraesw.demomoviewes.core.network.response.MovieCreditsListResponse
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCreditsCallHandler
@Inject constructor() {

//    companion object {
//        private var mInstance : MovieCreditsCallHandler? = null
//        fun getInstance() : MovieCreditsCallHandler {
//            mInstance ?: synchronized(this) {
//                mInstance = MovieCreditsCallHandler()
//            }
//            return mInstance!!
//        }
//    }

    @Inject
    lateinit var mMovieDbApi: MoviedbAPI

    @Inject
    lateinit var mGson: Gson

    suspend fun getMovieCredits(idMovie: Int): MovieCreditsListResponse {
        return coroutineScope {
            val item = async {
                val response = mMovieDbApi.fetchMovieCredit(idMovie, BuildConfig.MOVIES_DB_API).execute()

                when {
                    response.isSuccessful && response.body() != null ->
                        return@async response.body()!!
                    response.errorBody() != null ->
                        throw NetworkException(mGson.fromJson(response.errorBody()?.string(), NetworkError::class.java))
                    else ->
                        throw NetworkException(NetworkError("Unknown issue", 0))
                }
            }
            item.await()
        }
    }
}