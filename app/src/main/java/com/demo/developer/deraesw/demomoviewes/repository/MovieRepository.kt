package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Handler
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieToGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieToGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler

class MovieRepository private constructor(
        private val movieCallHandler: MovieCallHandler,
        private val movieDAO: MovieDAO,
        private val movieToGenreDAO: MovieToGenreDAO,
        private val appExecutors: AppExecutors){

    private val TAG = MovieRepository::class.java.simpleName

    val mMovieList : LiveData<List<Movie>> = movieDAO.selectAllMovies()
    val mMoviesInTheater : LiveData<List<MovieInTheater>> = movieDAO.selectMoviesInTheater()
    val mMovieInTheaterWithGenres : MutableLiveData<List<MovieInTheater>> = MutableLiveData()

    init {
        movieCallHandler.mMovieList.observeForever({
            if(it != null){
                appExecutors.diskIO().execute({
                    movieDAO.bulkInsertMovies(it)
                })

                handleMovieToGenreFromList(it)
            }
        })

        movieCallHandler.mMovie.observeForever({
            if(it != null){
                appExecutors.diskIO().execute({
                    movieDAO.insertMovie(it)
                })
            }
        })
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
                it.genres = movieToGenreDAO.selectGenreListFromMovie(it.id)
            })

            mMovieInTheaterWithGenres.postValue(list)
        })
    }

    private fun handleMovieToGenreFromList(list : List<Movie>){
        var movieToGenreList : List<MovieToGenre> = ArrayList()
        list.forEach({
            val idMovie = it.id
            it.genres.forEach({
                val idGenre = it.id
                movieToGenreList += MovieToGenre(idMovie, idGenre)
            })
        })

        appExecutors.diskIO().execute({
            movieToGenreDAO.bulkInsertMovieToGenre(movieToGenreList)
        })
    }

    companion object {
        @Volatile private var sInstance : MovieRepository? = null

        fun getInstance(
                movieCallHandler: MovieCallHandler ,
                movieDAO: MovieDAO,
                movieToGenreDAO: MovieToGenreDAO,
                appExecutors: AppExecutors) : MovieRepository {
            sInstance ?: synchronized(this){
                sInstance = MovieRepository(
                        movieCallHandler,
                        movieDAO,
                        movieToGenreDAO,
                        appExecutors)
            }

            return sInstance!!
        }
    }
}