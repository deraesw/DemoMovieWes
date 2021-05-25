package com.demo.developer.deraesw.demomoviewes.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.demo.developer.deraesw.demomoviewes.data.entity.Crew
import com.demo.developer.deraesw.demomoviewes.data.model.CrewItem

@Dao
interface CrewDAO : BaseDao<Crew> {

    @Query(
        "SELECT people.id, people.name, people.profilePath, crew.department ,crew.job " +
                "FROM crew " +
                "INNER JOIN people ON crew.peopleId = people.id " +
                "WHERE crew.movieId = :movieId "
    )
    fun selectCrewsItemFromMovie(movieId: Int): LiveData<List<CrewItem>>

    @Query(
        "SELECT people.id, people.name, people.profilePath, crew.department ,crew.job " +
                "FROM crew " +
                "INNER JOIN people ON crew.peopleId = people.id " +
            "WHERE crew.movieId = :movieId ")
    fun selectCrewsItemFromMovieWithpaging(movieId : Int) : DataSource.Factory<Int, CrewItem>

    @Query("DELETE FROM crew WHERE movieId = :movieId")
    suspend fun deleteCrewFromMovie(movieId: Int)

    @Query("DELETE FROM crew WHERE insertDate <> :date")
    suspend fun removeObsoleteCrew(date: String)
}