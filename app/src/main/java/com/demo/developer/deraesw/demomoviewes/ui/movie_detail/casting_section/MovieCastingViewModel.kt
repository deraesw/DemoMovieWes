package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.core.extension.whenFailed
import com.demo.developer.deraesw.demomoviewes.core.repository.MovieCreditsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieCastingViewModel @Inject constructor(
    private val movieCreditsRepository: MovieCreditsRepository
) : ViewModel() {

    private val eventChannel = Channel<MovieCastingViewModelEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun getMovieCasting(movieId: Int) =
        movieCreditsRepository.getCastingFromMovie(movieId).asLiveData()

    fun getLimitedMovieCasting(movieId: Int, limit: Int) =
        movieCreditsRepository.getLimitedCastingFromMovie(movieId, limit)

    fun fetchMovieCredits(movieId: Int) {
        viewModelScope.launch {
            movieCreditsRepository.fetchAndSaveMovieCredits(movieId).whenFailed {
                eventChannel.send(NetworkErrorEvent(it.errors))
            }
        }
    }
}

sealed class MovieCastingViewModelEvent
class NetworkErrorEvent(val networkError: NetworkError) : MovieCastingViewModelEvent()