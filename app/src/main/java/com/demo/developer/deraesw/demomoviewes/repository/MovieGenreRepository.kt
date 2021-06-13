package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface MovieGenreRepositoryInterface {
    val mMovieGenreList: Flow<List<MovieGenre>>
}

@Singleton
class MovieGenreRepository
@Inject constructor(
    private val networkRepository: NetworkRepository,
    private val movieGenreDAO: MovieGenreDAO
) : MovieGenreRepositoryInterface {

    override val mMovieGenreList = movieGenreDAO.selectAllMovieGenre()

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