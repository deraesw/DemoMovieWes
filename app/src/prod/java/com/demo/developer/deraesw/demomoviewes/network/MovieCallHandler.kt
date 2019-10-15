package com.demo.developer.deraesw.demomoviewes.network

import androidx.lifecycle.MutableLiveData
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.network.response.MoviesResponse
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import retrofit2.Call
import java.util.*
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

    private val mApi = BuildConfig.MOVIES_DB_API

    val mMovie : MutableLiveData<MovieResponse> = MutableLiveData()
    val errorMessage : SingleLiveEvent<NetworkError> = SingleLiveEvent()

    @Inject
    lateinit var mMovieDbApi: MoviedbAPI
    @Inject
    lateinit var mGson : Gson

    private fun callFetchNowPlayingMovies() = mMovieDbApi.fetchNowPlayingMovies(mApi, Locale.getDefault().country)

    private fun callFetchUpcomingMovies() = mMovieDbApi.fetchUpcomingMovies(mApi, Locale.getDefault().country)

    suspend fun getNowPlayingMovies(fromSync: Boolean = true) : List<MovieResponse> {
        return getMovies(callFetchNowPlayingMovies(), Constant.MovieFilterStatus.NOW_PLAYING_MOVIES, fromSync)
    }

    suspend fun getUpcomingMovies() : List<MovieResponse> {
        return getMovies(callFetchUpcomingMovies(), Constant.MovieFilterStatus.UPCOMING_MOVIES)
    }

    private suspend fun getMovies(call: Call<MoviesResponse>, filterStatus: Int, fromSync: Boolean = true) : List<MovieResponse> {
        return  coroutineScope {

            val list = async {
                val fullList = mutableListOf<MovieResponse>()
                val responseFullList = call.execute()
                when {
                    responseFullList.isSuccessful && responseFullList.body() != null -> {
                        responseFullList.body()?.results?.forEach { movie ->
                            fullList += getMovieResponseDetail(movie, filterStatus)
                            if(fromSync) delay(500)
                        }
                        return@async fullList
                    }
                    responseFullList.errorBody() != null ->
                        throw NetworkException(mGson.fromJson(responseFullList.errorBody()?.string(), NetworkError::class.java))
                    else ->
                        return@async fullList
                }
            }

            list.await()
        }
    }

    private suspend fun getMovieResponseDetail(movieItem: Movie, filterStatus : Int) : MovieResponse {
        return coroutineScope {
            val movieResponse = async {
                val response = mMovieDbApi.fetchMovieDetail(movieItem.id, mApi).execute()
                when {
                    response.isSuccessful && response.body() != null -> {
                        return@async response.body()?.apply {
                            this.filterStatus = filterStatus
                            this.insertDate = AppTools.getCurrentDate()
                        } ?: throw NetworkException(NetworkError("Unknown issue", 0))
                    }
                    else ->
                        throw NetworkException(mGson.fromJson(response.errorBody()?.string(), NetworkError::class.java))
                }
            }
            movieResponse.await()
        }
    }
}