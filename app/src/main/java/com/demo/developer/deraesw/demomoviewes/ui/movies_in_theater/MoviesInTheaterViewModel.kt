package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.core.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.core.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.core.extension.whenFailed
import com.demo.developer.deraesw.demomoviewes.core.repository.MovieGenreRepository
import com.demo.developer.deraesw.demomoviewes.core.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesInTheaterViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieGenreRepository: MovieGenreRepository
) : ViewModel() {

    val movieList: LiveData<List<MovieInTheater>> = movieRepository.moviesInTheater.asLiveData()
    val movieInTheaterWithGender: LiveData<List<MovieInTheater>> =
        movieRepository.movieInTheaterWithGenres
    val movieGenre: LiveData<List<MovieGenre>> = movieGenreRepository.mMovieGenreList.asLiveData()

    private val eventChannel = Channel<MoviesInTheaterViewModelEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun populateMovieInTheaterWithGenre(list: List<MovieInTheater>) {
        movieRepository.populateMovieInTheaterWithGenre(list)
    }

    fun fetchNowPlayingMoving() {
        viewModelScope.launch {
            movieRepository.fetchAndSaveNowPlayingMovies(fromSync = false).whenFailed {
                eventChannel.send(NetworkErrorEvent(it.errors))
            }
        }
    }
}

sealed class MoviesInTheaterViewModelEvent
class NetworkErrorEvent(val networkError: NetworkError) : MoviesInTheaterViewModelEvent()