package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import javax.inject.Inject

class FilterMovieViewModel
@Inject constructor (private val genreMovieRepository: MovieGenreRepository) : ViewModel() {
    private val TAG = FilterMovieViewModel::class.java.simpleName

    val movieGenreFilter : LiveData<List<GenreFilter>> = genreMovieRepository.mGenreForFilter
}