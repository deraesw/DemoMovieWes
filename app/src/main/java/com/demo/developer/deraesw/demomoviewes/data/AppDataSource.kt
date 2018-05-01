package com.demo.developer.deraesw.demomoviewes.data

import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.dao.*
import com.demo.developer.deraesw.demomoviewes.data.entity.*
import javax.inject.Inject

class AppDataSource constructor(
        val movieGenreDAO: MovieGenreDAO,
        val movieDAO: MovieDAO,
        val movieToGenreDAO: MovieToGenreDAO,
        val peopleDAO: PeopleDAO,
        val crewDAO: CrewDAO,
        val castingDAO: CastingDAO,
        val appExecutors: AppExecutors
) {

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
                        appExecutors)
            }

            return sInstance!!
        }

    }

    fun selectCastingItemFromMovie(movieId : Int) = castingDAO.selectCastingItemFromMovie(movieId)

    fun selectCrewItemFromMovie(movieId: Int) = crewDAO.selectCrewsItemFromMovie(movieId)

    fun saveListMovieGenre(list: List<MovieGenre>){
        appExecutors.diskIO().execute({
            movieGenreDAO.bulkInsertMovieGenre(list)
        })
    }

    fun saveMovie(movie: Movie){
        appExecutors.diskIO().execute({
            movieDAO.insertMovie(movie)
        })
    }

    fun saveListOfMovie(list: List<Movie>){
        appExecutors.diskIO().execute({
            movieDAO.bulkInsertMovies(list)
        })

        handleMovieToGenreFromList(list)
    }

    fun saveListPeople(list : List<People>) {
        appExecutors.diskIO().execute({
            peopleDAO.bulkInsertPeoples(list)
        })
    }

    fun saveListCasting(list : List<Casting>){
        appExecutors.diskIO().execute({
            val movieId = list.get(0).movieId ?: 0
            if(movieId != 0){
                castingDAO.deleteCastingFromMovie(movieId)
            }
            castingDAO.bulkInsertCastings(list)
        })
    }

    fun saveListCrew(list: List<Crew>){
        appExecutors.diskIO().execute({
            val movieId = list.get(0).movieId ?: 0
            if(movieId != 0){
                crewDAO.deleteCrewFromMovie(movieId)
            }
            crewDAO.bulkInsertCrews(list)
        })
    }

    fun saveListMovieToGenre(list: List<MovieToGenre>){
        appExecutors.diskIO().execute({
            movieToGenreDAO.bulkInsertMovieToGenre(list)
        })
    }

    private fun handleMovieToGenreFromList(list : List<Movie>){
        var movieToGenreList : List<MovieToGenre> = ArrayList()
        list.forEach({
            val idMovie = it.id
            it.genres.forEach({
                val idGenre = it.id
                movieToGenreList += MovieToGenre(idMovie, idGenre)
            })
        })

        saveListMovieToGenre(movieToGenreList)
    }


}