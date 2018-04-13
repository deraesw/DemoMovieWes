package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository

class FilterMovieViewModel (private val genreMovieRepository: MovieGenreRepository) : ViewModel() {
    private val TAG = FilterMovieViewModel::class.java.simpleName

    val movieGenre : LiveData<List<MovieGenre>> = genreMovieRepository.mMovieGenreList
}