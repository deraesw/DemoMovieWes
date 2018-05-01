package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section

import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.repository.MovieCreditsRepository
import javax.inject.Inject

class MovieCrewViewModel
@Inject constructor(
        private val movieCreditsRepository: MovieCreditsRepository) : ViewModel() {

    fun getMovieCrew(movieId: Int) = movieCreditsRepository.getCrewFromMovie(movieId)

    fun fetchMovieCredits(movieId : Int){
        movieCreditsRepository.fetchMovieCredits(movieId)
    }
}