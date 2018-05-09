package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository
@Inject constructor(
        private val movieCallHandler: MovieCallHandler,
        private val appDataSource: AppDataSource,
        private val appExecutors: AppExecutors){

    private val TAG = MovieRepository::class.java.simpleName

    val mMovieList : LiveData<List<Movie>> = appDataSource.movieDAO.selectAllMovies()
    val mMoviesInTheater : LiveData<List<MovieInTheater>> = appDataSource.movieDAO.selectMoviesInTheater()
    val mMovieInTheaterWithGenres : MutableLiveData<List<MovieInTheater>> = MutableLiveData()

    init {
        movieCallHandler.mMovieList.observeForever({
            if(it != null){
                appDataSource.saveListOfMovie(it)
            }
        })

        movieCallHandler.mMovie.observeForever({
            if(it != null){
                appDataSource.saveMovie(it)
            }
        })

        movieCallHandler.mMovieNetworkResponseList.observeForever({
            if(it != null){
                Log.d(TAG, " movieCallHandler.mMovieNetworkResponseList.observeForever : data found")
                appDataSource.saveListOfMovieNetworkResponse(it)
            }
        })
    }

    fun getMovieDetail(id : Int) = appDataSource.movieDAO.selectMovie(id)

    fun getMovieGenreFromMovie(idMovie : Int) : LiveData<List<MovieGenre>> {
        return appDataSource.movieToGenreDAO.observeGenreListFromMovie(idMovie)
    }

    fun fetchMovieDetail(id: Int){
        movieCallHandler.fetchMovieDetail(id)
    }

    fun fetchNowPlayingMovie(){
        movieCallHandler.fetchNowPlayingMovies()
    }

    fun populateMovieInTheaterWithGenre(list: List<MovieInTheater>){
        appExecutors.diskIO().execute({
            list.forEach({
                it.genres = appDataSource.movieToGenreDAO.selectGenreListFromMovie(it.id)
            })

            mMovieInTheaterWithGenres.postValue(list)
        })
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