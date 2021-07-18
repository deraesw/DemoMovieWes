package com.demo.developer.deraesw.demomoviewes.core.repository

import com.demo.developer.deraesw.demomoviewes.core.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.core.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.core.data.entity.People
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.core.network.handler.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.core.network.handler.MovieCreditsCallHandler
import com.demo.developer.deraesw.demomoviewes.core.network.handler.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.core.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.core.utils.Constant
import com.demo.developer.deraesw.demomoviewes.core.utils.getPeopleAndCastingList
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