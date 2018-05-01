package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.demo.developer.deraesw.demomoviewes.repository.MovieCreditsRepository

class MovieCastingFactory(
        private val movieCreditsRepository: MovieCreditsRepository,
        private val movieId : Int) : ViewModelProvider.Factory {

    private val TAG = MovieCastingFactory::class.java.simpleName

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieCastingViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MovieCastingViewModel(movieCreditsRepository, movieId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}