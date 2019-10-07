package com.demo.developer.deraesw.demomoviewes.data

import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.dao.*
import com.demo.developer.deraesw.demomoviewes.data.entity.*
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.network.response.MovieResponse
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.MapperUtils
import javax.inject.Inject

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

    private val TAG = AppDataSource::class.java.simpleName
    companion object {

        @Volatile private var sInstance : AppDataSource? = null

        @Inject fun getInstance( appDatabase: appDatabase,
                         appExecutors: AppExecutors) : AppDataSource {
            sInstance ?: synchronized(this){
                sInstance = AppDataSource(
                        appDatabase.movieGenreDao(),
                        appDatabase.movieDAO(),
                        appDatabase.movieToGenreDAO(),
                        appDatabase.peopleDAO(),
                        appDatabase.crewDAO(),
                        appDatabase.castingDAO(),
                        appDatabase.productionCompanyDao(),
                        appDatabase.movieToProductionDao(),
                        appExecutors)
            }

            return sInstance!!
        }

    }

    fun selectCastingItemFromMovie(movieId : Int) = castingDAO.selectCastingItemFromMovie(movieId)

    fun selectLimitedCastingItemFromMovie(movieId : Int, limit : Int) = castingDAO.selectLimitedCastingItemFromMovie(movieId, limit)

    fun selectCrewItemFromMovie(movieId: Int) = crewDAO.selectCrewsItemFromMovie(movieId)

    fun selectCrewItemFromMovieWithPaging(movieId: Int) = crewDAO.selectCrewsItemFromMovieWithpaging(movieId)

    fun selectProductionFromMovie(movieId: Int) = movieToProductionDao.selectProductionFromMovie(movieId)

    fun saveListMovieGenre(list: List<MovieGenre>){
        appExecutors.diskIO().execute {
            movieGenreDAO.bulkInsertMovieGenre(list)
        }
    }

    fun saveMovie(movie: Movie){
        appExecutors.diskIO().execute {
            movieDAO.insertMovie(movie)
        }
    }

    fun saveListOfMovie(list: List<Movie>){
        appExecutors.diskIO().execute {
            movieDAO.bulkInsertMovies(list)
        }

        handleMovieToGenreFromList(list)
    }

    fun saveListOfMovieNetworkResponse(list: List<MovieResponse>){
        appExecutors.diskIO().execute {
            list.forEach {
                val movie = it as Movie
                Log.d(TAG, "Save movie : ${movie.id} - ${movie.title}")
                movieDAO.insertMovie(movie)

                var movieToGenreList : List<MovieToGenre> = ArrayList()
                it.genres.forEach {
                    movieToGenreList += MovieToGenre(idMovie = movie.id, idGenre = it.id)
                }
                saveListMovieToGenre(movieToGenreList)

                if(it.production_companies != null){
                    var prodComp = it.production_companies!!
                    prodComp.forEach { item -> item.insertDate = AppTools.getCurrentDate() }
                    saveListProductionCompany(prodComp)
                    var listMovieProduction : List<MovieToProduction> = listOf()
                    it.production_companies!!.forEach {
                        listMovieProduction += MovieToProduction(idMovie = movie.id, idProduction = it.id)
                    }
                    saveListMovieToProduction(listMovieProduction)
                }

                it.credits?.apply {
                    if(cast.count() > 0) {
                        handleCastResponse(cast, movie.id)
                    }
                    if(crew.count() > 0) {
                        //handleCrewResponse(crew, movie.id)
                    }
                }
            }
            movieDAO.removeObsoleteMovies(AppTools.getCurrentDate())
        }
    }

    fun saveListPeople(list : List<People>) {
        appExecutors.diskIO().execute {
            peopleDAO.bulkInsertPeoples(list)
            peopleDAO.removeObsoletePeople(AppTools.getCurrentDate())
        }
    }

    fun saveListCasting(list : List<Casting>){
        appExecutors.diskIO().execute {
            val movieId = list.get(0).movieId ?: 0
            if(movieId != 0){
                castingDAO.deleteCastingFromMovie(movieId)
            }
            castingDAO.bulkInsertCastings(list)
            castingDAO.removeObsoleteCasting(AppTools.getCurrentDate())
        }
    }

    fun saveListCrew(list: List<Crew>){
        appExecutors.diskIO().execute {
            val movieId = list.get(0).movieId ?: 0
            if(movieId != 0){
                crewDAO.deleteCrewFromMovie(movieId)
            }
            crewDAO.bulkInsertCrews(list)
        }
    }

    fun saveListMovieToGenre(list: List<MovieToGenre>){
        appExecutors.diskIO().execute {
            movieToGenreDAO.bulkInsertMovieToGenre(list)
        }
    }

    fun saveListProductionCompany(list: List<ProductionCompany>){
        appExecutors.diskIO().execute {
            productionCompanyDao.bulkIgnoreInsert(list)
            productionCompanyDao.removeObsoleteProduction(AppTools.getCurrentDate())
        }
    }

    fun saveListMovieToProduction(list: List<MovieToProduction>){
        appExecutors.diskIO().execute {
            movieToProductionDao.bulkForceInsert(list)
        }
    }

    fun cleanAllData() {
        appExecutors.diskIO().execute {
            movieDAO.removeAllMovies()
            peopleDAO.deleteAll()
            productionCompanyDao.deleteAll()
            movieGenreDAO.removeAllData()
        }
    }

    private fun handleCastResponse(list : List<MovieCreditsListResponse.Casting>, movieId: Int){
        var peopleList : List<People> = listOf()
        var castList : List<Casting> = listOf()

        list.forEach {
            peopleList += MapperUtils.Data.mapCastResponseToPeople(it)
            castList += MapperUtils.Data.mapCastResponseToCasting(it, movieId)
        }

        saveListPeople(peopleList)
        saveListCasting(castList)
    }

    private fun handleCrewResponse(list : List<MovieCreditsListResponse.Crew>, movieId : Int){
        var peopleList : List<People> = listOf()
        var crewList : List<Crew> = listOf()

        list.forEach {
            peopleList += MapperUtils.Data.mapCrewResponseToPeople(it)
            crewList += MapperUtils.Data.mapCrewResponseToCrew(it, movieId)
        }

        saveListPeople(peopleList)
        saveListCrew(crewList)
    }

    private fun handleMovieToGenreFromList(list : List<Movie>){
        var movieToGenreList : List<MovieToGenre> = ArrayList()
        list.forEach {
            val idMovie = it.id
            it.genres.forEach {
                val idGenre = it.id
                movieToGenreList += MovieToGenre(idMovie, idGenre)
            }
        }

        saveListMovieToGenre(movieToGenreList)
    }


}