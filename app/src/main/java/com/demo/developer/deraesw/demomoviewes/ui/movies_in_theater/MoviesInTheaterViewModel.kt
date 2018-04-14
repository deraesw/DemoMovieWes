package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository

class MoviesInTheaterViewModel(
        private val movieRepository: MovieRepository,
        private val movieGenreRepository : MovieGenreRepository) : ViewModel() {

    val mMovieList : LiveData<List<MovieInTheater>> = movieRepository.mMoviesInTheater
    val mMovieInTheaterWithGender : LiveData<List<MovieInTheater>> = movieRepository.mMovieInTheaterWithGenres
    val mMovieGenre : LiveData<List<MovieGenre>> = movieGenreRepository.mMovieGenreList

    fun populateMovieInTheaterWithGenre(list: List<MovieInTheater>){
        movieRepository.populateMovieInTheaterWithGenre(list)
    }
}