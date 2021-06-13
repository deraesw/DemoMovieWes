package com.demo.developer.deraesw.demomoviewes.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.UpcomingMovie
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepositoryInterface
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieGenreRepository: MovieGenreRepositoryInterface
) : ViewModel() {

    val upcomingMovieList: LiveData<List<UpcomingMovie>> =
        movieRepository.upcomingMovies.asLiveData()
    val upcomingMoviesWithGender: LiveData<List<UpcomingMovie>> =
        movieRepository.upcomingMoviesWithGenres
    val movieGenre: LiveData<List<MovieGenre>> = movieGenreRepository.mMovieGenreList.asLiveData()

    fun populateUpcomingMoviesWithGenre(list: List<UpcomingMovie>) {
        movieRepository.populateUpcomingMoviesWithGenre(list)
    }
}