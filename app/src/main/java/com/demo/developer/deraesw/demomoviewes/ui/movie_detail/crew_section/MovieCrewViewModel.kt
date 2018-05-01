package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.model.CrewItem
import com.demo.developer.deraesw.demomoviewes.repository.MovieCreditsRepository

class MovieCrewViewModel(
        private val movieCreditsRepository: MovieCreditsRepository,
        private val idMovie : Int) : ViewModel() {

    val crew : LiveData<List<CrewItem>> = movieCreditsRepository.getCrewFromMovie(idMovie)

    fun fetchMovieCredits(movieId : Int){
        movieCreditsRepository.fetchMovieCredits(movieId)
    }
}