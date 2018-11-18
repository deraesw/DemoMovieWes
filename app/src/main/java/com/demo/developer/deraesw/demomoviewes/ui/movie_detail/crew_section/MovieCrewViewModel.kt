package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.demo.developer.deraesw.demomoviewes.repository.MovieCreditsRepository
import javax.inject.Inject

class MovieCrewViewModel
@Inject constructor(
        private val movieCreditsRepository: MovieCreditsRepository) : ViewModel() {

    fun getMovieCrew(movieId: Int) = movieCreditsRepository.getCrewFromMovie(movieId)

    fun getMovieCrewWithPaging(movieId: Int) = LivePagedListBuilder(
            movieCreditsRepository.getCrewFromMovieWithPaging(movieId),
            20)
            .build()


    fun fetchMovieCredits(movieId : Int){
        movieCreditsRepository.fetchMovieCredits(movieId)
    }

}