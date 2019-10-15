package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesInTheaterViewModel
@Inject constructor(
        private val movieRepository: MovieRepository,
        private val movieGenreRepository : MovieGenreRepository) : ViewModel() {

    val movieList : LiveData<List<MovieInTheater>> = movieRepository.moviesInTheater
    val movieInTheaterWithGender : LiveData<List<MovieInTheater>> = movieRepository.mMovieInTheaterWithGenres
    val movieGenre : LiveData<List<MovieGenre>> = movieGenreRepository.mMovieGenreList
    val errorMessage = movieRepository.errorMessage

    fun populateMovieInTheaterWithGenre(list: List<MovieInTheater>){
        movieRepository.populateMovieInTheaterWithGenre(list)
    }

    fun fetchNowPlayingMoving() {
        viewModelScope.launch {
            movieRepository.fetchAndSaveNowPlayingMovies(fromSync = false)
        }
    }
}