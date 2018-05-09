package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section

import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.repository.MovieCreditsRepository
import javax.inject.Inject

class MovieCastingViewModel
@Inject constructor(
        private val movieCreditsRepository: MovieCreditsRepository) : ViewModel() {

    val errorNetwork = movieCreditsRepository.errorNetwork

    fun getMovieCasting(movieId: Int) = movieCreditsRepository.getCastingFromMovie(movieId)

    fun fetchMovieCredits(movieId : Int){
        movieCreditsRepository.fetchMovieCredits(movieId)
    }
}