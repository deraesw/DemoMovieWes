package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository

class MovieDetailFactory(private val movieRepository: MovieRepository,val movieId : Int) : ViewModelProvider.Factory {
    private val TAG = MovieDetailFactory::class.java.simpleName

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MovieDetailViewModel(movieRepository, movieId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}