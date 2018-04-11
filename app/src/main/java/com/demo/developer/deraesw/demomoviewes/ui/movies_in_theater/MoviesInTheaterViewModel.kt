package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository

class MoviesInTheaterViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    val mMovieList : LiveData<List<MovieInTheater>> = movieRepository.mMoviesInTheater

}