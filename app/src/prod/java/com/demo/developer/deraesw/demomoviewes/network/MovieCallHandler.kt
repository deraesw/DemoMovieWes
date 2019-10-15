package com.demo.developer.deraesw.demomoviewes.network

import androidx.lifecycle.MutableLiveData
import android.os.Handler
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.BuildConfig
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.entity.ProductionCompany
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
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
import retrofit2.Callback
import retrofit2.Response
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

    private val TAG = MovieCallHandler::class.java.simpleName
    private val mApi = BuildConfig.MOVIES_DB_API

    val mMovieList : MutableLiveData<List<Movie>> = MutableLiveData()
    val mMovie : MutableLiveData<MovieResponse> = MutableLiveData()
    val mMovieNetworkResponseList : MutableLiveData<List<MovieResponse>> = MutableLiveData()
    val errorMessage : SingleLiveEvent<NetworkError> = SingleLiveEvent()

    @Inject
    lateinit var mMovieDbApi: MoviedbAPI
    @Inject
    lateinit var mGson : Gson

    fun fetchMovieDetail(id : Int){
        val call = mMovieDbApi.fetchMovieDetail(id, mApi)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Log.e(TAG, t?.message, t)
                //todo
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if(response.isSuccessful) {
                    val movie = response.body()
                    if(movie != null){
                        mMovie.postValue(movie)
                    }
                }
            }
        })
    }

    private fun callFetchNowPlayingMovies() = mMovieDbApi.fetchNowPlayingMovies(mApi, Locale.getDefault().country)

    fun fetchNowPlayingMovies() {
        val call = callFetchNowPlayingMovies()

        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val list = response.body()?.results
                        if(list != null){
                            handleFetchingMovieDetailFromList(list, Constant.MovieFilterStatus.NOW_PLAYING_MOVIES)
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        Log.w(TAG, "Empty body")
                        val error = mGson.fromJson(response.errorBody()?.string(), NetworkError::class.java)
                        errorMessage.postValue(error)
                    }
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.e(TAG, t.message, t)
                errorMessage.postValue(NetworkError(t.message ?: "unknown error", 0))
            }
        })
    }

    suspend fun getNowPlayingMovies() : List<MovieResponse> {
        return  coroutineScope {

            val list = async {
                val fullList = mutableListOf<MovieResponse>()
                debug("Call response getNowPlayingMovies")
                val responseFullList = callFetchNowPlayingMovies().execute()
                debug("Call response received")
                when {
                    responseFullList.isSuccessful && responseFullList.body() != null -> {
                        responseFullList.body()?.results?.forEach { movie ->
                            debug("Call response movie detail - ${movie.id}")
                            fullList += getMovieResponseDetail(movie, Constant.MovieFilterStatus.NOW_PLAYING_MOVIES)
                            delay(1000)
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

    private fun callFetchUpcomingMovies() = mMovieDbApi.fetchUpcomingMovies(mApi, Locale.getDefault().country)

    fun fetchUpcomingMovies() {
        val call = callFetchUpcomingMovies()

        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val list = response.body()?.results
                        if(list != null){
                            handleFetchingMovieDetailFromList(list, Constant.MovieFilterStatus.UPCOMING_MOVIES)
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        Log.w(TAG, "Empty body")
                        val error = mGson.fromJson(response.errorBody()?.string(), NetworkError::class.java)
                        errorMessage.postValue(error)
                    }
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.e(TAG, t.message, t)
                errorMessage.postValue(NetworkError(t.message ?: "unknown error", 0))
            }
        })
    }

    fun fetchNowPlayingResponse() :  Response<MoviesResponse> {
        return callFetchNowPlayingMovies().execute()
    }

    fun fetchingMovieDetailFromList(movieList: List<Movie>) : List<MovieResponse> {
        var completeMovieList : List<MovieResponse> = listOf()

        val handler = Handler()
        movieList.forEach {
            handler.postDelayed({
                val res = mMovieDbApi.fetchMovieDetail(it.id, mApi).execute()
                if(res.isSuccessful){
                    val movie = res.body()
                    if(movie != null){
                        completeMovieList += movie
                    }
                } else {
                    Log.d(TAG, "something went wrong")
                    errorMessage.postValue(NetworkError("unknown error", 0))
                }

            }, 500)
        }

        return completeMovieList
    }

    private fun handleFetchingMovieDetailFromList(movieList: List<Movie>, filterStatus : Int) {
        var completeMovieList : List<MovieResponse> = listOf()

        val handler = Handler()
        movieList.forEach {
            handler.postDelayed({
                debug("start process " + it.id)
                val call = mMovieDbApi.fetchMovieDetail(it.id, mApi)

                call.enqueue(object : Callback<MovieResponse> {
                    override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                        Log.e(TAG, t?.message, t)
                    }

                    override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                        if(response.isSuccessful) {
                            val movie = response.body()
                            if(movie != null){
                                movie.filterStatus = filterStatus
                                movie.insertDate = AppTools.getCurrentDate()
                                completeMovieList += movie
                                if(movieList.size == completeMovieList.size){
                                    mMovieNetworkResponseList.postValue(completeMovieList)
                                }
                            }
                        } else {
                            if (response.errorBody() != null) {
                                val error = mGson.fromJson(response.errorBody()?.string(), NetworkError::class.java)
                                errorMessage.postValue(error)
                            }
                        }
                    }
                })
            }, 1000)
        }
    }
}