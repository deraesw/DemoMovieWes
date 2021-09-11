package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.demo.developer.deraesw.demomoviewes.core.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun getMovieDetail(movieId: Int) = movieRepository.getMovieDetail(movieId)

    fun getGenreFromMovie(movieId: Int) =
        movieRepository.getMovieGenreFromMovie(movieId).asLiveData()

    fun getProductionFromMovie(movieId: Int) = movieRepository.getProductionFromMovie(movieId)
}