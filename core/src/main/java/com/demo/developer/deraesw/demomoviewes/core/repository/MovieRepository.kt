package com.demo.developer.deraesw.demomoviewes.core.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.demo.developer.deraesw.demomoviewes.core.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.core.data.dao.*
import com.demo.developer.deraesw.demomoviewes.core.data.entity.*
import com.demo.developer.deraesw.demomoviewes.core.data.model.*
import com.demo.developer.deraesw.demomoviewes.core.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.core.utils.Constant
import com.demo.developer.deraesw.demomoviewes.core.utils.DateUtils
import com.demo.developer.deraesw.demomoviewes.core.utils.getPeopleAndCastingList
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository
@Inject constructor(
    private val appDataSource: AppDataSource,
    private val networkRepository: NetworkRepository,
    private val movieDAO: MovieDAO,
    private val movieToGenreDAO: MovieToGenreDAO,
    private val movieToProductionDao: MovieToProductionDao,
    private val castingDAO: CastingDAO,
    private val peopleDAO: PeopleDAO,
    private val productionCompanyDao: ProductionCompanyDao
) {

    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val movieInTheaterWithGenres: MutableLiveData<List<MovieInTheater>> = MutableLiveData()
    val upcomingMoviesWithGenres: MutableLiveData<List<UpcomingMovie>> = MutableLiveData()

    val movieList = movieDAO.selectAllMovies()
    val moviesInTheater = movieDAO.selectMoviesInTheater()
    val upcomingMovies = movieDAO.selectUpcomingMovies()

    fun getMovieDetail(id: Int) = movieDAO.selectMovie(id).asLiveData()

    fun getProductionFromMovie(movieId: Int) =
        movieToProductionDao.selectProductionFromMovie(movieId)

    fun getMovieGenreFromMovie(idMovie: Int) = movieToGenreDAO.observeGenreListFromMovie(idMovie)

    suspend fun fetchAndSaveNowPlayingMovies(fromSync: Boolean = true): NetworkResults {
        return fetchAndSaveMovies(Constant.MovieType.NOW_PLAYING_MOVIES, fromSync)
    }

    suspend fun fetchAndSaveUpcomingMovies(): NetworkResults {
        return fetchAndSaveMovies(Constant.MovieType.UPCOMING_MOVIES)
    }

    private suspend fun fetchAndSaveMovies(
        movieType: Constant.MovieType,
        fromSync: Boolean = false
    ): NetworkResults {
        return withContext(Dispatchers.IO) {
            val result = networkRepository.fetchMovies(movieType, fromSync)
            if (result.errors != null) {
                return@withContext NetworkFailed(result.errors)
            }

            result.data?.also {
                saveListOfMovieNetworkResponse(it)
            }
            return@withContext NetworkSuccess(true)
        }
    }

    fun populateMovieInTheaterWithGenre(list: List<MovieInTheater>) {
        scope.launch {
            list.forEach {
                it.genres = movieToGenreDAO.selectGenreListFromMovie(it.id)
            }

            movieInTheaterWithGenres.postValue(list)
        }
    }

    fun populateUpcomingMoviesWithGenre(list: List<UpcomingMovie>) {
        scope.launch {
            list.forEach {
                it.genres = movieToGenreDAO.selectGenreListFromMovie(it.id)
            }

            upcomingMoviesWithGenres.postValue(list)
        }
    }

    suspend fun cleanAllData() {
        appDataSource.cleanAllData()
    }

    private suspend fun saveListOfMovieNetworkResponse(list: List<MovieResponse>) {
        withContext(Dispatchers.IO) {
            list.forEach {
                val movie = it as Movie

                movieDAO.insert(movie)

                saveMovieGenre(it.genres, movie.id)
                saveProductionMovie(it)
                saveCasting(it)
            }
            movieDAO.removeObsoleteMovies(DateUtils.getCurrentDate())
        }
    }

    private suspend fun saveMovieGenre(list: List<MovieGenre>, movieId: Int) {
        val movieToGenreList = mutableListOf<MovieToGenre>()
        list.forEach { movieGenre ->
            movieToGenreList += MovieToGenre(idMovie = movieId, idGenre = movieGenre.id)
        }

        movieToGenreDAO.bulkForceInsert(movieToGenreList)
    }

    private suspend fun saveProductionMovie(movieResponse: MovieResponse) {
        val currentDate = DateUtils.getCurrentDate()
        movieResponse.production_companies?.also { productionCompany: List<ProductionCompany> ->
            val listMovieProduction: MutableList<MovieToProduction> = mutableListOf()
            productionCompany.forEach { item ->
                item.insertDate = currentDate
                listMovieProduction += MovieToProduction(
                    idMovie = movieResponse.id,
                    idProduction = item.id
                )
            }

            productionCompanyDao.saveListProductionCompany(productionCompany)
            movieToProductionDao.bulkForceInsert(listMovieProduction)
        }
    }

    private suspend fun saveCasting(movieResponse: MovieResponse) {
        movieResponse.credits?.cast?.takeIf { cast -> cast.isNotEmpty() }?.also { cast ->
            val (peoples, castings) = getPeopleAndCastingList(cast, movieResponse.id)
            peopleDAO.saveListPeople(peoples)
            castingDAO.saveListCasting(castings, movieResponse.id)
        }
    }
}