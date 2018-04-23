package com.demo.developer.deraesw.demomoviewes.utils

import android.content.Context
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.MainActivityFactory
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieCreditsCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.repository.*
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailFactory
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section.MovieCastingFactory
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterFactory
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterMoviesFactory

object Injection {

    fun provideMainRepository(context: Context) : MainRepository{
        val genreRepository = provideMovieGenreRepository(context)
        val sharePrefRepository = provideSharePrefRepository(context)
        val movieRepository = provideMovieRepository(context)
        return MainRepository.getInstance(genreRepository, sharePrefRepository, movieRepository)
    }

    fun  provideSharePrefRepository(context: Context) : SharePrefRepository{
        return SharePrefRepository.getInstance(context.applicationContext)
    }

    fun provideMovieGenreRepository(context: Context) : MovieGenreRepository {
        val database = appDatabase.getInstance(context.applicationContext)
        val movieGenreCallHandler = MovieGenreCallHandler.getInstance()
        val appExecutors = AppExecutors.getInstance()
        val appDataSource = AppDataSource.getInstance(database, appExecutors)
        return MovieGenreRepository.getInstance(movieGenreCallHandler, appDataSource ,appExecutors)
    }

    fun provideMovieCreditsRepository(context: Context) : MovieCreditsRepository {
        val database = appDatabase.getInstance(context.applicationContext)
        val movieCreditsCallHandler = MovieCreditsCallHandler.getInstance()
        val appExecutors = AppExecutors.getInstance()
        val appDataSource = AppDataSource.getInstance(database, appExecutors)
        return MovieCreditsRepository.getInstance(movieCreditsCallHandler, appDataSource, appExecutors)
    }

    fun provideMovieRepository(context: Context) : MovieRepository {
        val database = appDatabase.getInstance(context.applicationContext)
        val movieCallHandler = MovieCallHandler.getInstance()
        val appExecutors = AppExecutors.getInstance()
        val appDataSource = AppDataSource.getInstance(database, appExecutors)
        return MovieRepository.getInstance(movieCallHandler, appDataSource , appExecutors)
    }

    fun provideMainActivityFactory(context: Context) : MainActivityFactory{
        return MainActivityFactory(provideMainRepository(context))
    }

    fun provideMovieInTheaterFactory(context: Context) : MoviesInTheaterFactory {
        return MoviesInTheaterFactory(
                provideMovieRepository(context),
                provideMovieGenreRepository(context))
    }

    fun provideMovieDetailFactory(context: Context, movieId : Int) : MovieDetailFactory {
        return MovieDetailFactory(provideMovieRepository(context), movieId)
    }

    fun provideFilterMoviesFactory(context: Context) : FilterMoviesFactory {
        return FilterMoviesFactory(provideMovieGenreRepository(context))
    }

    fun provideMovieCastingFactory(context: Context, movieId : Int) : MovieCastingFactory {
        return MovieCastingFactory(
                provideMovieCreditsRepository(context),
                movieId
        )
    }
}