package com.demo.developer.deraesw.demomoviewes.data

import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.dao.*
import com.demo.developer.deraesw.demomoviewes.data.entity.*
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.MapperUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppDataSource constructor(
    val movieGenreDAO: MovieGenreDAO,
    val movieDAO: MovieDAO,
    val movieToGenreDAO: MovieToGenreDAO,
    private val peopleDAO: PeopleDAO,
    private val crewDAO: CrewDAO,
    private val castingDAO: CastingDAO,
    private val productionCompanyDao: ProductionCompanyDao,
    private val movieToProductionDao: MovieToProductionDao,
    private val appExecutors: AppExecutors
) {

    companion object {

        @Volatile
        private var sInstance: AppDataSource? = null

        fun getInstance(
            AppDatabase: AppDatabase,
            appExecutors: AppExecutors
        ): AppDataSource {
            sInstance ?: synchronized(this) {
                sInstance = AppDataSource(
                    AppDatabase.movieGenreDao(),
                    AppDatabase.movieDAO(),
                    AppDatabase.movieToGenreDAO(),
                    AppDatabase.peopleDAO(),
                    AppDatabase.crewDAO(),
                    AppDatabase.castingDAO(),
                    AppDatabase.productionCompanyDao(),
                        AppDatabase.movieToProductionDao(),
                        appExecutors
                )
            }

            return sInstance!!
        }

    }

//    fun selectCastingItemFromMovie(movieId: Int) = castingDAO.selectCastingItemFromMovie(movieId)
//
//    fun selectLimitedCastingItemFromMovie(movieId: Int, limit: Int) =
//        castingDAO.selectLimitedCastingItemFromMovie(movieId, limit)
//
//    fun selectCrewItemFromMovie(movieId: Int) = crewDAO.selectCrewsItemFromMovie(movieId)
//
//    fun selectCrewItemFromMovieWithPaging(movieId: Int) =
//        crewDAO.selectCrewsItemFromMovieWithpaging(movieId)

    fun selectProductionFromMovie(movieId: Int) =
            movieToProductionDao.selectProductionFromMovie(movieId)

    suspend fun saveListMovieGenre(list: List<MovieGenre>) {
        withContext(Dispatchers.IO) {
            movieGenreDAO.bulkForceInsert(list)
        }
    }

//    fun saveMovie(movie: Movie){
//        appExecutors.diskIO().execute {
//            movieDAO.insertMovie(movie)
//        }
//    }

//    fun saveListOfMovie(list: List<Movie>){
//        appExecutors.diskIO().execute {
//            movieDAO.bulkInsertMovies(list)
//        }
//
//        handleMovieToGenreFromList(list)
//    }

    suspend fun saveListOfMovieNetworkResponse(list: List<MovieResponse>) {
        withContext(Dispatchers.IO) {

            list.forEach {
                val movie = it as Movie
                debug("Save movie : ${movie.id} - ${movie.title}")
                movieDAO.insert(movie)

                val movieToGenreList = mutableListOf<MovieToGenre>()
                it.genres.forEach { movieGenre ->
                    movieToGenreList += MovieToGenre(idMovie = movie.id, idGenre = movieGenre.id)
                }
                movieToGenreDAO.bulkForceInsert(movieToGenreList)

                val currentDate = AppTools.getCurrentDate()
                it.production_companies?.also { productionCompany: List<ProductionCompany> ->
                    val listMovieProduction: MutableList<MovieToProduction> = mutableListOf()
                    productionCompany.forEach { item ->
                        item.insertDate = currentDate
                        listMovieProduction += MovieToProduction(
                            idMovie = movie.id,
                            idProduction = item.id
                        )
                    }
                    saveListProductionCompany(productionCompany)
                    saveListMovieToProduction(listMovieProduction)
                }

                it.credits?.apply {
                    if (cast.count() > 0) {
                        handleCastResponse(cast, movie.id)
                    }
                }
            }
            movieDAO.removeObsoleteMovies(AppTools.getCurrentDate())
        }
    }

    suspend fun saveListPeople(list: List<People>) {
        withContext(Dispatchers.IO) {
            peopleDAO.bulkForceInsert(list)
            peopleDAO.removeObsoletePeople(AppTools.getCurrentDate())
        }
    }

    suspend fun saveListCasting(list: List<Casting>) {
        withContext(Dispatchers.IO) {
            val movieId = list[0].movieId
            if (movieId != 0) {
                castingDAO.deleteCastingFromMovie(movieId)
            }
            castingDAO.bulkForceInsert(list)
            castingDAO.removeObsoleteCasting(AppTools.getCurrentDate())
        }
    }

    fun saveListCrew(list: List<Crew>) {
//        appExecutors.diskIO().execute {
//            val movieId = list.get(0).movieId ?: 0
//            if (movieId != 0) {
//                crewDAO.deleteCrewFromMovie(movieId)
//            }
//            crewDAO.bulkInsertCrews(list)
//        }
    }

    private suspend fun saveListMovieToGenre(list: List<MovieToGenre>) {
        withContext(Dispatchers.IO) {
            movieToGenreDAO.bulkIgnoreInsert(list)
        }
    }

    private suspend fun saveListProductionCompany(list: List<ProductionCompany>) {
        withContext(Dispatchers.IO) {
            productionCompanyDao.bulkForceInsert(list)
            productionCompanyDao.removeObsoleteProduction(AppTools.getCurrentDate())
        }
    }

    private suspend fun saveListMovieToProduction(list: List<MovieToProduction>) {
        withContext(Dispatchers.IO) {
            movieToProductionDao.bulkForceInsert(list)
        }
    }

    suspend fun cleanAllData() {
        withContext(Dispatchers.IO) {
            debug("removeAllMovies - s")
            movieDAO.removeAllMovies()
            debug("removeAllMovies - e")
            debug("peopleDAO.deleteAll() - s")
            peopleDAO.deleteAll()
            debug("peopleDAO.deleteAll() - e")
            debug("productionCompanyDao.deleteAll - s")
            productionCompanyDao.deleteAll()
            debug("productionCompanyDao.deleteAll - e")
            debug("removeAllData - s")
            movieGenreDAO.removeAllData()
            debug("removeAllData - e")
        }
    }

    private suspend fun handleCastResponse(
        list: List<MovieCreditsListResponse.Casting>,
        movieId: Int
    ) {
        val peopleList: MutableList<People> = mutableListOf()
        val castList: MutableList<Casting> = mutableListOf()

        list.forEach {
            peopleList += MapperUtils.Data.mapCastResponseToPeople(it)
            castList += MapperUtils.Data.mapCastResponseToCasting(it, movieId)
        }

        saveListPeople(peopleList)
        saveListCasting(castList)
    }

//    private fun handleCrewResponse(list : List<MovieCreditsListResponse.Crew>, movieId : Int){
//        var peopleList : List<People> = listOf()
//        var crewList : List<Crew> = listOf()
//
//        list.forEach {
//            peopleList += MapperUtils.Data.mapCrewResponseToPeople(it)
//            crewList += MapperUtils.Data.mapCrewResponseToCrew(it, movieId)
//        }
//
//        saveListPeople(peopleList)
//        saveListCrew(crewList)
//    }

//    private fun handleMovieToGenreFromList(list : List<Movie>){
//        var movieToGenreList : List<MovieToGenre> = ArrayList()
//        list.forEach {
//            val idMovie = it.id
//            it.genres.forEach {
//                val idGenre = it.id
//                movieToGenreList += MovieToGenre(idMovie, idGenre)
//            }
//        }
//
//        saveListMovieToGenre(movieToGenreList)
//    }


}