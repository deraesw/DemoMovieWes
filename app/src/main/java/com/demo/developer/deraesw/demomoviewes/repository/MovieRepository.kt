package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.data.model.UpcomingMovie
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository
@Inject constructor(
        private val movieCallHandler: MovieCallHandler,
        private val appDataSource: AppDataSource,
        private val networkRepository: NetworkRepository
) {

    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val errorMessage = movieCallHandler.errorMessage
    val mMovieInTheaterWithGenres: MutableLiveData<List<MovieInTheater>> = MutableLiveData()
    val upcomingMoviesWithGenres: MutableLiveData<List<UpcomingMovie>> = MutableLiveData()

    val movieList: LiveData<List<Movie>> =
            appDataSource.movieDAO.selectAllMovies()
    val moviesInTheater: LiveData<List<MovieInTheater>> =
            appDataSource.movieDAO.selectMoviesInTheater()
    val upcomingMovies: LiveData<List<UpcomingMovie>> =
            appDataSource.movieDAO.selectUpcomingMovies()

    fun getMovieDetail(id: Int) = appDataSource.movieDAO.selectMovie(id)

    fun getProductionFromMovie(movieId: Int) = appDataSource.selectProductionFromMovie(movieId)

    fun getMovieGenreFromMovie(idMovie: Int): LiveData<List<MovieGenre>> {
        return appDataSource.movieToGenreDAO.observeGenreListFromMovie(idMovie)
    }

    suspend fun fetchAndSaveNowPlayingMovies(fromSync: Boolean = true): Boolean {
        return fetchAndSaveMovies(Constant.MovieType.NOW_PLAYING_MOVIES, fromSync)
    }

    suspend fun fetchAndSaveUpcomingMovies(): Boolean {
        return fetchAndSaveMovies(Constant.MovieType.UPCOMING_MOVIES)
    }

    private suspend fun fetchAndSaveMovies(movieType: Constant.MovieType, fromSync: Boolean = false): Boolean {
        return withContext(Dispatchers.IO) {
            val result = networkRepository.fetchMovies(movieType, fromSync)
            if (result.errors != null) {
                errorMessage.postValue(result.errors)
                return@withContext false
            }

            result.data?.also {
                appDataSource.saveListOfMovieNetworkResponse(it)
            }
            return@withContext true
        }
    }

    fun populateMovieInTheaterWithGenre(list: List<MovieInTheater>) {
        scope.launch {
            list.forEach {
                it.genres = appDataSource.movieToGenreDAO.selectGenreListFromMovie(it.id)
            }

            mMovieInTheaterWithGenres.postValue(list)
        }
    }

    fun populateUpcomingMoviesWithGenre(list: List<UpcomingMovie>) {
        scope.launch {
            list.forEach {
                it.genres = appDataSource.movieToGenreDAO.selectGenreListFromMovie(it.id)
            }

            upcomingMoviesWithGenres.postValue(list)
        }
    }

    suspend fun cleanAllData() {
        appDataSource.cleanAllData()
    }
}