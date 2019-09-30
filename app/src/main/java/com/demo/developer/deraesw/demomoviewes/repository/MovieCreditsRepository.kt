package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.entity.Crew
import com.demo.developer.deraesw.demomoviewes.data.entity.People
import com.demo.developer.deraesw.demomoviewes.network.MovieCreditsCallHandler
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.utils.MapperUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCreditsRepository
@Inject constructor(
        private val movieCreditsCallHandler: MovieCreditsCallHandler,
        private val appDataSource: AppDataSource,
        private val appExecutors: AppExecutors){

    private val TAG = MovieCreditsRepository::class.java.simpleName

    val errorNetwork = movieCreditsCallHandler.mErrorMessage

    init {
        movieCreditsCallHandler.mCreditsList.observeForever {
            if(it != null){
                if(it.cast.isNotEmpty()){
                    handleCastResponse(it.cast, it.id)
                }

                if(it.crew.isNotEmpty()){
                    //handleCrewResponse(it.crew, it.id)
                }
            }
        }

    }

    fun getCastingFromMovie(movieId : Int) = appDataSource.selectCastingItemFromMovie(movieId)

    fun getLimitedCastingFromMovie(movieId : Int, limit : Int) = appDataSource.selectLimitedCastingItemFromMovie(movieId, limit)

    fun getCrewFromMovie(movieId: Int) = appDataSource.selectCrewItemFromMovie(movieId)

    fun getCrewFromMovieWithPaging(movieId: Int) = appDataSource.selectCrewItemFromMovieWithPaging(movieId)

    fun fetchMovieCredits(id: Int){
        movieCreditsCallHandler.fetchMovieCredits(id)
    }

    private fun handleCastResponse(list : List<MovieCreditsListResponse.Casting>, movieId: Int){
        var peopleList : List<People> = ArrayList()
        var castList : List<Casting> = ArrayList()

        list.forEach {
            peopleList += MapperUtils.Data.mapCastResponseToPeople(it)
            castList += MapperUtils.Data.mapCastResponseToCasting(it, movieId)
        }

        appDataSource.saveListPeople(peopleList)
        appDataSource.saveListCasting(castList)
    }

    private fun handleCrewResponse(list : List<MovieCreditsListResponse.Crew>, movieId : Int){
        var peopleList : List<People> = ArrayList()
        var crewList : List<Crew> = ArrayList()

        list.forEach {
            peopleList += MapperUtils.Data.mapCrewResponseToPeople(it)
            crewList += MapperUtils.Data.mapCrewResponseToCrew(it, movieId)
        }

        appDataSource.saveListPeople(peopleList)
        appDataSource.saveListCrew(crewList)
    }

    companion object {
        @Volatile private var sInstance : MovieCreditsRepository? = null

        fun getInstance(
                movieCreditsCallHandler: MovieCreditsCallHandler ,
                appDataSource: AppDataSource,
                appExecutors: AppExecutors) : MovieCreditsRepository {
            sInstance ?: synchronized(this){
                sInstance = MovieCreditsRepository(
                        movieCreditsCallHandler,
                        appDataSource,
                        appExecutors)
            }

            return sInstance!!
        }
    }
}