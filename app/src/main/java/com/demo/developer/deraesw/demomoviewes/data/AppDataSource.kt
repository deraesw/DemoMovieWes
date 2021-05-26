package com.demo.developer.deraesw.demomoviewes.data

import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.dao.*
import com.demo.developer.deraesw.demomoviewes.extension.debug
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

}