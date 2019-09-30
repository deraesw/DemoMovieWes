package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieGenreRepository
@Inject constructor(
        private val movieGenreCallHandler: MovieGenreCallHandler ,
        private val appDataSource: AppDataSource,
        private val appExecutors: AppExecutors){

    private val TAG = MovieGenreRepository::class.java.simpleName

    var syncInformationMessage : SingleLiveEvent<String> = SingleLiveEvent()
    val mMovieGenreList : LiveData<List<MovieGenre>> = appDataSource.movieGenreDAO.selectAllMovieGenre()
    val mGenreForFilter : LiveData<List<GenreFilter>> = appDataSource.movieGenreDAO.selectAllMovieGenreForFilter()

    init {
        movieGenreCallHandler.mMovieGenreList.observeForever {
            if(it != null){
                appDataSource.saveListMovieGenre(it)
            }
        }
    }

    fun fetchAllMovieGenreData() {
        syncInformationMessage.postValue("Fetching movie genre list...")
        movieGenreCallHandler.fetchGenreMovieList()
    }

    fun synchronizeMovieGenre() : Boolean {
        val response = movieGenreCallHandler.fetchGenreMovieResponse()
        if(response.isSuccessful){
            val movieResponse = response.body()
            if(movieResponse != null) {
                appDataSource.saveListMovieGenre(movieResponse.genres)
                return true
            }
        }

        return false
    }

    companion object {
        @Volatile private var sInstance : MovieGenreRepository? = null

        fun getInstance(
                movieGenreCallHandler: MovieGenreCallHandler ,
                appDataSource: AppDataSource,
                appExecutors: AppExecutors) : MovieGenreRepository {
            sInstance ?: synchronized(this){
                sInstance = MovieGenreRepository(
                        movieGenreCallHandler,
                        appDataSource,
                        appExecutors)
            }

            return sInstance!!
        }
    }
}