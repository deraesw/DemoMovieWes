package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.dao.CastingDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.PeopleDAO
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkFailed
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkResults
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCreditsRepository
@Inject constructor(
        private val castingDAO: CastingDAO,
        private val peopleDAO: PeopleDAO,
        private val networkRepository: NetworkRepository
) {

    fun getCastingFromMovie(movieId: Int) = castingDAO.selectCastingItemFromMovie(movieId)

    fun getLimitedCastingFromMovie(
        movieId: Int,
        limit: Int
    ) = castingDAO.selectLimitedCastingItemFromMovie(movieId, limit)

    suspend fun fetchAndSaveMovieCredits(id: Int): NetworkResults {
        return withContext(Dispatchers.IO) {
            val result = networkRepository.fetchMovieCredits(id = id)
            if (result.errors != null) {
                return@withContext NetworkFailed(result.errors)
            }

            result.data?.also {
                peopleDAO.saveListPeople(it.peoples)
                castingDAO.saveListCasting(it.castings, it.movieId)
            }

            return@withContext NetworkSuccess(true)
        }
    }
}