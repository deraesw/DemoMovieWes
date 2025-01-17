package com.demo.developer.deraesw.demomoviewes.core.data

import com.demo.developer.deraesw.demomoviewes.core.data.dao.MovieDAO
import com.demo.developer.deraesw.demomoviewes.core.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.core.data.dao.PeopleDAO
import com.demo.developer.deraesw.demomoviewes.core.data.dao.ProductionCompanyDao
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
            movieDAO.removeAllMovies()
            peopleDAO.deleteAll()
            productionCompanyDao.deleteAll()
            movieGenreDAO.removeAllData()
        }
    }

}