package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.data.model.UpcomingMovie
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository
@Inject constructor(
        private val movieCallHandler: MovieCallHandler,
        private val appDataSource: AppDataSource,
        private val appExecutors: AppExecutors){

    var syncInformationMessage : SingleLiveEvent<String> = SingleLiveEvent()
    val mMovieInTheaterWithGenres : MutableLiveData<List<MovieInTheater>> = MutableLiveData()
    val upcomingMoviesWithGenres : MutableLiveData<List<UpcomingMovie>> = MutableLiveData()

    val movieList : LiveData<List<Movie>> =
            appDataSource.movieDAO.selectAllMovies()
    val moviesInTheater : LiveData<List<MovieInTheater>> =
            appDataSource.movieDAO.selectMoviesInTheater()
    val upcomingMovies : LiveData<List<UpcomingMovie>> =
            appDataSource.movieDAO.selectUpcomingMovies()

    init {
        movieCallHandler.mMovieList.observeForever {
            if(it != null){
                appDataSource.saveListOfMovie(it)
            }
        }

        movieCallHandler.mMovie.observeForever {
            if(it != null){
                appDataSource.saveMovie(it)
            }
        }

        movieCallHandler.mMovieNetworkResponseList.observeForever {
            if(it != null){
                debug(" movieCallHandler.mMovieNetworkResponseList.observeForever : data found")
                appDataSource.saveListOfMovieNetworkResponse(it)
            }
        }
    }

    fun getMovieDetail(id : Int) = appDataSource.movieDAO.selectMovie(id)

    fun getProductionFromMovie(movieId : Int) = appDataSource.selectProductionFromMovie(movieId)

    fun getMovieGenreFromMovie(idMovie : Int) : LiveData<List<MovieGenre>> {
        return appDataSource.movieToGenreDAO.observeGenreListFromMovie(idMovie)
    }

    fun fetchMovieDetail(id: Int){
        appExecutors.networkIO().execute {
            movieCallHandler.fetchMovieDetail(id)
        }
    }

    fun fetchNowPlayingMovie() {
        syncInformationMessage.postValue("Fetching movies in theaters...")
        appExecutors.networkIO().execute {
            movieCallHandler.fetchNowPlayingMovies()
        }
    }

    fun fetchUpcomingMovies() {
        syncInformationMessage.postValue("Fetching upcoming movies...")
        appExecutors.networkIO().execute {
            movieCallHandler.fetchUpcomingMovies()
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