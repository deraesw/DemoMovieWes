package com.demo.developer.deraesw.demomoviewes.utils

import android.content.Context
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.MainActivityFactory
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import com.demo.developer.deraesw.demomoviewes.repository.SharePrefRepository

object Injection {

    fun provideMainRepository(context: Context) : MainRepository{
        val genreRepository = provideMovieGenreRepository(context)
        val sharePrefRepository = provideSharePrefRepository(context.applicationContext)

        return MainRepository.getInstance(genreRepository, sharePrefRepository)
    }

    fun  provideSharePrefRepository(context: Context) : SharePrefRepository{
        return SharePrefRepository.getInstance(context)
    }

    fun provideMovieGenreRepository(context: Context) : MovieGenreRepository {
        val database = appDatabase.getInstance(context)
        val movieGenreCallHandler = MovieGenreCallHandler.getInstance();
        val appExecutors = AppExecutors.getInstance()
        return MovieGenreRepository.getInstance(movieGenreCallHandler, database.movieGenreDao() ,appExecutors)
    }

    fun provideMainActivityFactory(context: Context) : MainActivityFactory{
        return MainActivityFactory(provideMainRepository(context))
    }

}