package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieGenreRepository
@Inject constructor(
    private val networkRepository: NetworkRepository,
    private val movieGenreDAO: MovieGenreDAO
) {

    val mMovieGenreList: LiveData<List<MovieGenre>> = movieGenreDAO.selectAllMovieGenre()
    val mGenreForFilter: LiveData<List<GenreFilter>> = movieGenreDAO.selectAllMovieGenreForFilter()

    var errorMessage: NetworkError? = null
        get() {
            return field.let {
                field = null
                it
            }
        }


    suspend fun fetchAndSaveMovieGenreInformation(): Boolean {
        return withContext(Dispatchers.IO) {
            val result = networkRepository.fetchMoviesGenreInformation()

            if (result.errors != null) {
                errorMessage = result.errors
                return@withContext false
            }

            result.data?.also {
                movieGenreDAO.bulkForceInsert(it)
            }
            return@withContext true
        }
    }
}