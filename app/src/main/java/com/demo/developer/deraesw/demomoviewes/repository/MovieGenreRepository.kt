package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.LiveData
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler

class MovieGenreRepository private constructor(
        private val movieGenreCallHandler: MovieGenreCallHandler ,
        private val movieGenreDAO: MovieGenreDAO,
        private val appExecutors: AppExecutors){

    private val TAG = MovieGenreRepository::class.java.simpleName

    val mMovieGenreList : LiveData<List<MovieGenre>> = movieGenreDAO.selectAllMovieGenre()
    val mGenreForFilter : LiveData<List<GenreFilter>> = movieGenreDAO.selectAllMovieGenreForFilter()

    init {
        movieGenreCallHandler.mMovieGenreList.observeForever({
            if(it != null){
                appExecutors.diskIO().execute({
                    movieGenreDAO.bulkInsertMovieGenre(it)
                })

            }
        })
    }

    fun fetchAllMovieGenreData(){
        movieGenreCallHandler.fetchGenreMovieList()
    }

    companion object {
        @Volatile private var sInstance : MovieGenreRepository? = null

        fun getInstance(
                movieGenreCallHandler: MovieGenreCallHandler ,
                movieGenreDAO: MovieGenreDAO,
                appExecutors: AppExecutors) : MovieGenreRepository {
            sInstance ?: synchronized(this){
                sInstance = MovieGenreRepository(
                        movieGenreCallHandler,
                        movieGenreDAO,
                        appExecutors)
            }

            return sInstance!!
        }
    }
}