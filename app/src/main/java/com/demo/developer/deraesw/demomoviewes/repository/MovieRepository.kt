package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.data.model.UpcomingMovie
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository
@Inject constructor(
        private val movieCallHandler: MovieCallHandler,
        private val appDataSource: AppDataSource,
        private val appExecutors: AppExecutors){

    val errorMessage = movieCallHandler.errorMessage
    var syncInformationMessage : SingleLiveEvent<String> = SingleLiveEvent()
    val mMovieInTheaterWithGenres : MutableLiveData<List<MovieInTheater>> = MutableLiveData()
    val upcomingMoviesWithGenres : MutableLiveData<List<UpcomingMovie>> = MutableLiveData()

    val movieList : LiveData<List<Movie>> =
            appDataSource.movieDAO.selectAllMovies()
    val moviesInTheater : LiveData<List<MovieInTheater>> =
            appDataSource.movieDAO.selectMoviesInTheater()
    val upcomingMovies : LiveData<List<UpcomingMovie>> =
            appDataSource.movieDAO.selectUpcomingMovies()

    fun getMovieDetail(id : Int) = appDataSource.movieDAO.selectMovie(id)

    fun getProductionFromMovie(movieId : Int) = appDataSource.selectProductionFromMovie(movieId)

    fun getMovieGenreFromMovie(idMovie : Int) : LiveData<List<MovieGenre>> {
        return appDataSource.movieToGenreDAO.observeGenreListFromMovie(idMovie)
    }

    suspend fun fetchAndSaveNowPlayingMovies(fromSync: Boolean = true): Boolean {
        return withContext(Dispatchers.IO) {
            val res = async {
                if(fromSync) syncInformationMessage.postValue("Fetching movies in theaters...")
                try {
                    val moviesList = movieCallHandler.getNowPlayingMovies(fromSync)
                    appDataSource.saveListOfMovieNetworkResponse(moviesList)
                    true
                } catch (net: NetworkException) {
                    errorMessage.postValue(NetworkError(net.message!!, 0))
                    false
                    //todo
                } catch (io: IOException) {
                    errorMessage.postValue(NetworkError(io.message!!, 0))
                    false
                    //todo
                }
            }
            res.await()
        }
    }

    suspend fun fetchAndSaveUpcomingMovies(): Boolean {
        syncInformationMessage.postValue("Fetching upcoming movies...")
        return try {
            val moviesList = movieCallHandler.getUpcomingMovies()
            appDataSource.saveListOfMovieNetworkResponse(moviesList)
            true
        } catch (net: NetworkException) {
            errorMessage.postValue(NetworkError(net.message!!, 0))
            false
            //todo
        } catch (io: IOException) {
            errorMessage.postValue(NetworkError(io.message!!, 0))
            false
            //todo
        }
    }

    fun populateMovieInTheaterWithGenre(list: List<MovieInTheater>){
        appExecutors.diskIO().execute {
            list.forEach {
                it.genres = appDataSource.movieToGenreDAO.selectGenreListFromMovie(it.id)
            }

            mMovieInTheaterWithGenres.postValue(list)
        }
    }

    fun populateUpcomingMoviesWithGenre(list: List<UpcomingMovie>){
        appExecutors.diskIO().execute {
            list.forEach {
                it.genres = appDataSource.movieToGenreDAO.selectGenreListFromMovie(it.id)
            }

            upcomingMoviesWithGenres.postValue(list)
        }
    }

    suspend fun cleanAllData() {
        appDataSource.cleanAllData()
    }


    companion object {
        @Volatile private var sInstance : MovieRepository? = null

        fun getInstance(
                movieCallHandler: MovieCallHandler ,
                appDataSource: AppDataSource,
                appExecutors: AppExecutors) : MovieRepository {
            sInstance ?: synchronized(this){
                sInstance = MovieRepository(
                        movieCallHandler,
                        appDataSource,
                        appExecutors)
            }

            return sInstance!!
        }
    }
}