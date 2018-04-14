package com.demo.developer.deraesw.demomoviewes.utils

import android.content.Context
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.MainActivityFactory
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository
import com.demo.developer.deraesw.demomoviewes.repository.SharePrefRepository
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailFactory
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterFactory
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterMoviesFactory

object Injection {

    fun provideMainRepository(context: Context) : MainRepository{
        val genreRepository = provideMovieGenreRepository(context)
        val sharePrefRepository = provideSharePrefRepository(context.applicationContext)
        val movieRepository = provideMovieRepository(context)
        return MainRepository.getInstance(genreRepository, sharePrefRepository, movieRepository)
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

    fun provideMovieRepository(context: Context) : MovieRepository {
        val database = appDatabase.getInstance(context)
        val movieCallHandler = MovieCallHandler.getInstance()
        val appExecutors = AppExecutors.getInstance()
        return MovieRepository.getInstance(movieCallHandler, database.movieDAO(), database.movieToGenreDAO(), appExecutors)
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
}