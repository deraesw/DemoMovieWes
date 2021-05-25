package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.dao.CastingDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.entity.People
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkException
import com.demo.developer.deraesw.demomoviewes.network.MovieCreditsCallHandler
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse
import com.demo.developer.deraesw.demomoviewes.utils.MapperUtils
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieCreditsRepository
@Inject constructor(
    private val movieCreditsCallHandler: MovieCreditsCallHandler,
    private val appDataSource: AppDataSource,
    private val castingDAO: CastingDAO,
    private val appExecutors: AppExecutors
){

    val errorNetwork: SingleLiveEvent<NetworkError> = SingleLiveEvent()

    fun getCastingFromMovie(movieId: Int) = castingDAO.selectCastingItemFromMovie(movieId)

    fun getLimitedCastingFromMovie(movieId: Int, limit: Int) =
        castingDAO.selectLimitedCastingItemFromMovie(movieId, limit)

//    fun getCrewFromMovie(movieId: Int) = appDataSource.selectCrewItemFromMovie(movieId)

//    fun getCrewFromMovieWithPaging(movieId: Int) = appDataSource.selectCrewItemFromMovieWithPaging(movieId)

    suspend fun fetchAndSaveMovieCredits(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                val credits = movieCreditsCallHandler.getMovieCredits(id)
                handleCastResponse(credits.cast, credits.id)
//            true
            } catch (net: NetworkException) {
                errorNetwork.postValue(NetworkError(net.message!!, 0))
//            false
                //todo
            } catch (io: IOException) {
                errorNetwork.postValue(NetworkError(io.message!!, 0))
//            false
                //todo
            }
        }
    }

    private suspend fun handleCastResponse(list : List<MovieCreditsListResponse.Casting>, movieId: Int){
        val peopleList : MutableList<People> = mutableListOf()
        val castList : MutableList<Casting> = mutableListOf()

        list.forEach {
            peopleList += MapperUtils.Data.mapCastResponseToPeople(it)
            castList += MapperUtils.Data.mapCastResponseToCasting(it, movieId)
        }

        appDataSource.saveListPeople(peopleList)
        appDataSource.saveListCasting(castList)
    }

//    private fun handleCrewResponse(list : List<MovieCreditsListResponse.Crew>, movieId : Int){
//        var peopleList : List<People> = ArrayList()
//        var crewList : List<Crew> = ArrayList()
//
//        list.forEach {
//            peopleList += MapperUtils.Data.mapCrewResponseToPeople(it)
//            crewList += MapperUtils.Data.mapCrewResponseToCrew(it, movieId)
//        }
//
//        appDataSource.saveListPeople(peopleList)
//        appDataSource.saveListCrew(crewList)
//    }

}