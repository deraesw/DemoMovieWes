package com.demo.developer.deraesw.demomoviewes.repository

import androidx.lifecycle.LiveData
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
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

    //    var syncInformationMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val mMovieGenreList: LiveData<List<MovieGenre>> = movieGenreDAO.selectAllMovieGenre()
    val mGenreForFilter: LiveData<List<GenreFilter>> = movieGenreDAO.selectAllMovieGenreForFilter()

    val errorMessage: SingleLiveEvent<String> = SingleLiveEvent()

    suspend fun fetchAndSaveMovieGenreInformation(): Boolean {
//        syncInformationMessage.postValue("Fetching movie genre list...")
        return withContext(Dispatchers.IO) {
            val result = networkRepository.fetchMoviesGenreInformation()
            if (result.errors != null) {
                errorMessage.postValue(result.errors.statusMessage)
                return@withContext false
            }

            result.data?.also {
                movieGenreDAO.bulkForceInsert(it)
            }
            return@withContext true
        }
    }
}