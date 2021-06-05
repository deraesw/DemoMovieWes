package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.*
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

    suspend fun fetchAndSaveMovieGenreInformation(): NetworkResults {
        return withContext(Dispatchers.IO) {
            val result = networkRepository.fetchMoviesGenreInformation()

            if (result.errors != null) {
                return@withContext NetworkFailed(result.errors)
            }

            result.data?.also {
                movieGenreDAO.bulkForceInsert(it)
            }
            return@withContext NetworkSuccess(true)
        }
    }
}