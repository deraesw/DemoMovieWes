package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.LiveData
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler

class MovieGenreRepository private constructor(
        private val movieGenreCallHandler: MovieGenreCallHandler ,
        private val appDataSource: AppDataSource,
        private val appExecutors: AppExecutors){

    private val TAG = MovieGenreRepository::class.java.simpleName

    val mMovieGenreList : LiveData<List<MovieGenre>> = appDataSource.movieGenreDAO.selectAllMovieGenre()
    val mGenreForFilter : LiveData<List<GenreFilter>> = appDataSource.movieGenreDAO.selectAllMovieGenreForFilter()

    init {
        movieGenreCallHandler.mMovieGenreList.observeForever({
            if(it != null){
                appDataSource.saveListMovieGenre(it)
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