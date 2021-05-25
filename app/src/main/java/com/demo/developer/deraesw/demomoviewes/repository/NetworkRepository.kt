package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.entity.People
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieCreditsCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.utils.MapperUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository
@Inject constructor(
        private val movieCallHandler: MovieCallHandler,
        private val movieCreditsCallHandler: MovieCreditsCallHandler,
        private val movieGenreCallHandler: MovieGenreCallHandler
) {

    suspend fun fetchMovieCredits(id: Int): MovieCreditResult {
        return withContext(Dispatchers.IO) {
            try {
                val credits = movieCreditsCallHandler.getMovieCredits(id)
                val (peoples, castings) = getPeopleAndCastingList(credits.cast, id)

                return@withContext MovieCreditResult(
                        data = MovieCreditData(
                                movieId = id,
                                peoples = peoples,
                                castings = castings
                        )
                )
            } catch (net: NetworkException) {
                return@withContext MovieCreditResult(
                        errors = (NetworkError(net.message ?: "", 0))
                )
            } catch (io: IOException) {
                return@withContext MovieCreditResult(
                        errors = (NetworkError(io.message ?: "", 0))
                )
            }
        }
    }

    private fun getPeopleAndCastingList(
            list: List<MovieCreditsListResponse.Casting>,
            movieId: Int
    ): Pair<List<People>, List<Casting>> {

        val peopleList: MutableList<People> = mutableListOf()
        val castList: MutableList<Casting> = mutableListOf()

        list.forEach {
            peopleList += MapperUtils.Data.mapCastResponseToPeople(it)
            castList += MapperUtils.Data.mapCastResponseToCasting(it, movieId)
        }

        return Pair(peopleList, castList)
    }

}

data class MovieCreditResult(
        val data: MovieCreditData? = null,
        val errors: NetworkError? = null
)

data class MovieCreditData(
        val movieId: Int,
        val peoples: List<People> = listOf(),
        val castings: List<Casting> = listOf(),
)