package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler

class MovieRepository private constructor(
        private val movieCallHandler: MovieCallHandler,
        private val movieDAO: MovieDAO,
        private val appExecutors: AppExecutors){

    private val TAG = MovieRepository::class.java.simpleName

    val mMovieList : LiveData<List<Movie>> = movieDAO.selectAllMovies()
    val mMoviesInTheater : LiveData<List<MovieInTheater>> = movieDAO.selectMoviesInTheater()

    init {
        movieCallHandler.mMovieList.observeForever({
            if(it != null){
                appExecutors.diskIO().execute({
                    movieDAO.bulkInsertMovies(it)
                })

            }
        })
    }

    fun fetchNowPlayingMovie(){
        movieCallHandler.fetchNowPlayingMovies()
    }

    companion object {
        @Volatile private var sInstance : MovieRepository? = null

        fun getInstance(
                movieCallHandler: MovieCallHandler ,
                movieDAO: MovieDAO,
                appExecutors: AppExecutors) : MovieRepository {
            sInstance ?: synchronized(this){
                sInstance = MovieRepository(
                        movieCallHandler,
                        movieDAO,
                        appExecutors)
            }

            return sInstance!!
        }
    }
}