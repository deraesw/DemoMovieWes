package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.dao.CastingDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.PeopleDAO
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
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

    val errorNetwork: SingleLiveEvent<NetworkError> = SingleLiveEvent()

    fun getCastingFromMovie(movieId: Int) = castingDAO.selectCastingItemFromMovie(movieId)

    fun getLimitedCastingFromMovie(
            movieId: Int,
            limit: Int
    ) = castingDAO.selectLimitedCastingItemFromMovie(movieId, limit)

    suspend fun fetchAndSaveMovieCredits(id: Int) {
        withContext(Dispatchers.IO) {
            val result = networkRepository.fetchMovieCredits(id = id)
            if (result.errors != null) {
                errorNetwork.postValue(result.errors)
                return@withContext
            }

            result.data?.also {
                peopleDAO.saveListPeople(it.peoples)
                castingDAO.saveListCasting(it.castings, it.movieId)
            }
        }
    }
}