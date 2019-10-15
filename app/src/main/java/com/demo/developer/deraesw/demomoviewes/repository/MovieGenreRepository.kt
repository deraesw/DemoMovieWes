package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.extension.error
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import kotlinx.coroutines.withContext
import java.io.IOException
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

    val errorMessage : SingleLiveEvent<String> = SingleLiveEvent()

    init {
        movieGenreCallHandler.mMovieGenreList.observeForever {
            if(it != null){
                //appDataSource.saveListMovieGenre(it)
            }
        }
    }

    fun fetchAllMovieGenreData() {
        syncInformationMessage.postValue("Fetching movie genre list...")
        movieGenreCallHandler.fetchGenreMovieList()
    }

    suspend fun fetchAndSaveMovieGenreInformation(): Boolean {
        syncInformationMessage.postValue("Fetching movie genre list...")
        return try {
            debug("fetchAndSaveMovieGenreInformation")
            val list = movieGenreCallHandler.getGenreMovieList()
            debug("fetchAndSaveMovieGenreInformation ${list.size}")
            appDataSource.saveListMovieGenre(list)
            debug("fetchAndSaveMovieGenreInformation data save")
            true
        } catch (net: NetworkException) {
            errorMessage.postValue(net.message)
            false
        } catch (io: IOException) {
            errorMessage.postValue(io.message)
            false
        }
    }

    fun synchronizeMovieGenre() : Boolean {
        val response = movieGenreCallHandler.fetchGenreMovieResponse()
        if(response.isSuccessful){
            val movieResponse = response.body()
            if(movieResponse != null) {
                //TODO
                //appDataSource.saveListMovieGenre(movieResponse.genres)
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