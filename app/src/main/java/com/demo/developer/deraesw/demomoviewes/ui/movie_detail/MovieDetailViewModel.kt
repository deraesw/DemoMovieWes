package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository

class MovieDetailViewModel(val movieRepository: MovieRepository, val idMovie : Int) : ViewModel() {

    val movie : LiveData<Movie> = movieRepository.getMovieDetail(idMovie)
    val genreFromMovie : LiveData<List<MovieGenre>> = movieRepository.getMovieGenreFromMovie(idMovie)

}