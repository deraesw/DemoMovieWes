package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository
import javax.inject.Inject

class MovieDetailViewModel
@Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    fun getMovieDetail(movieId : Int) = movieRepository.getMovieDetail(movieId)

    fun getGenreFromMovie(movieId : Int) = movieRepository.getMovieGenreFromMovie(movieId)
}