package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository

class FilterMoviesFactory(private val movieGenreRepository: MovieGenreRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FilterMovieViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FilterMovieViewModel(movieGenreRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}