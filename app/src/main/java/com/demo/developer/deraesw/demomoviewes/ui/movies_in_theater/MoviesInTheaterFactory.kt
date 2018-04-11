package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository

@Suppress("UNCHECKED_CAST")
class MoviesInTheaterFactory(private val movieRepository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoviesInTheaterViewModel::class.java)){
            return MoviesInTheaterViewModel(movieRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}