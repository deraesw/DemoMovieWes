package com.demo.developer.deraesw.demomoviewes.data

import com.demo.developer.deraesw.demomoviewes.data.dao.MovieDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.PeopleDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.ProductionCompanyDao
import com.demo.developer.deraesw.demomoviewes.extension.debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppDataSource constructor(
    val movieGenreDAO: MovieGenreDAO,
    val movieDAO: MovieDAO,
    private val peopleDAO: PeopleDAO,
    private val productionCompanyDao: ProductionCompanyDao,
) {

    companion object {

        @Volatile
        private var sInstance: AppDataSource? = null

        fun getInstance(
            AppDatabase: AppDatabase
        ): AppDataSource {
            sInstance ?: synchronized(this) {
                sInstance = AppDataSource(
                    AppDatabase.movieGenreDao(),
                    AppDatabase.movieDAO(),
                    AppDatabase.peopleDAO(),
                    AppDatabase.productionCompanyDao()
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