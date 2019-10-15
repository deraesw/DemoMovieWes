package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import androidx.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository
import javax.inject.Inject

class MovieDetailViewModel
@Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun getMovieDetail(movieId : Int) = mainRepository.movieRepository.getMovieDetail(movieId)

    fun getGenreFromMovie(movieId : Int) = mainRepository.movieRepository.getMovieGenreFromMovie(movieId)

    fun getProductionFromMovie(movieId: Int) = mainRepository.movieRepository.getProductionFromMovie(movieId)
}