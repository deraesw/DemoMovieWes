package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.entity.People
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieCreditsCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.utils.Constant
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

    suspend fun fetchMovies(movieType: Constant.MovieType, fromSync: Boolean = false): MovieResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val list = when (movieType) {
                    Constant.MovieType.NOW_PLAYING_MOVIES -> movieCallHandler.getNowPlayingMovies(fromSync)
                    Constant.MovieType.UPCOMING_MOVIES -> movieCallHandler.getUpcomingMovies()
                }

                MovieResult(data = list)
            } catch (net: NetworkException) {
                MovieResult(errors = (NetworkError(net.message ?: "", 0)))
            } catch (io: IOException) {
                MovieResult(errors = (NetworkError(io.message ?: "", 0)))
            }
        }
    }

    suspend fun fetchMoviesGenreInformation(): MovieGenreResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val list = movieGenreCallHandler.getGenreMovieList()

                MovieGenreResult(data = list)
            } catch (net: NetworkException) {
                MovieGenreResult(errors = (NetworkError(net.message ?: "", 0)))
            } catch (io: IOException) {
                MovieGenreResult(errors = (NetworkError(io.message ?: "", 0)))
            }
        }
    }

    suspend fun fetchMovieCredits(id: Int): MovieCreditResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val credits = movieCreditsCallHandler.getMovieCredits(id)
                val (peoples, castings) = getPeopleAndCastingList(credits.cast, id)

                MovieCreditResult(
                        data = MovieCreditData(
                                movieId = id,
                                peoples = peoples,
                                castings = castings
                        )
                )
            } catch (net: NetworkException) {
                MovieCreditResult(errors = (NetworkError(net.message ?: "", 0)))
            } catch (io: IOException) {
                MovieCreditResult(errors = (NetworkError(io.message ?: "", 0)))
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

data class MovieResult(
        val data: List<MovieResponse>? = null,
        val errors: NetworkError? = null
)

data class MovieGenreResult(
        val data: List<MovieGenre>? = null,
        val errors: NetworkError? = null
)

data class MovieCreditResult(
        val data: MovieCreditData? = null,
        val errors: NetworkError? = null
)

data class MovieCreditData(
        val movieId: Int,
        val peoples: List<People> = listOf(),
        val castings: List<Casting> = listOf(),
)