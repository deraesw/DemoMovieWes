package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.demo.developer.deraesw.demomoviewes.repository.MovieCreditsRepository

class MovieCrewFactory(
        private val movieCreditsRepository: MovieCreditsRepository,
        private val movieId : Int) : ViewModelProvider.Factory {

    private val TAG = MovieCrewFactory::class.java.simpleName

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieCrewViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MovieCrewViewModel(movieCreditsRepository, movieId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}