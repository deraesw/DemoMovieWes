package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.data.model.CrewItem
import com.demo.developer.deraesw.demomoviewes.repository.MovieCreditsRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository

class MovieCrewViewModel(
        private val movieCreditsRepository: MovieCreditsRepository,
        private val idMovie : Int) : ViewModel() {

    val crew : LiveData<List<CrewItem>> = movieCreditsRepository.getCrewFromMovie(idMovie)

    fun fetchMovieCredits(movieId : Int){
        movieCreditsRepository.fetchMovieCredits(movieId)
    }
}