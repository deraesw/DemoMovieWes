package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieToGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.*
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieCreditsCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.utils.MapperUtils

class MovieCreditsRepository private constructor(
        private val movieCreditsCallHandler: MovieCreditsCallHandler,
        private val appDataSource: AppDataSource,
        private val appExecutors: AppExecutors){

    private val TAG = MovieCreditsRepository::class.java.simpleName


    init {
        movieCreditsCallHandler.mCreditsList.observeForever({
            if(it != null){
                if(it.cast.isNotEmpty()){
                    handleCastResponse(it.cast, it.id)
                }
            }
        })
    }

    fun getCastingFromMovie(movieId : Int) = appDataSource.selectCastingItemFromMovie(movieId)

    fun fetchMovieCredits(id: Int){
        movieCreditsCallHandler.fetchMovieCredits(id)
    }

    private fun handleCastResponse(list : List<MovieCreditsListResponse.Casting>, movieId: Int){
        var peopleList : List<People> = ArrayList()
        var castList : List<Casting> = ArrayList()

        list.forEach {
            peopleList += MapperUtils.Data.mapCastResponseToPeople(it)
            castList += MapperUtils.Data.mapCastResponseToCasting(it, movieId)
        }

        appDataSource.saveListPeople(peopleList)
        appDataSource.saveListCasting(castList)
    }


    companion object {
        @Volatile private var sInstance : MovieCreditsRepository? = null

        fun getInstance(
                movieCreditsCallHandler: MovieCreditsCallHandler ,
                appDataSource: AppDataSource,
                appExecutors: AppExecutors) : MovieCreditsRepository {
            sInstance ?: synchronized(this){
                sInstance = MovieCreditsRepository(
                        movieCreditsCallHandler,
                        appDataSource,
                        appExecutors)
            }

            return sInstance!!
        }
    }
}