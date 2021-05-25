package com.demo.developer.deraesw.demomoviewes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.developer.deraesw.demomoviewes.data.dao.*
import com.demo.developer.deraesw.demomoviewes.data.entity.*

@Database(
    entities = [
        (MovieGenre::class),
        (Movie::class),
        (MovieToGenre::class),
        (People::class),
        (Crew::class),
        (Casting::class),
        (ProductionCompany::class),
        (MovieToProduction::class)
    ],
    version = 7
)
abstract class appDatabase : RoomDatabase() {

    abstract fun movieGenreDao(): MovieGenreDAO
    abstract fun movieDAO(): MovieDAO
    abstract fun movieToGenreDAO(): MovieToGenreDAO
    abstract fun peopleDAO(): PeopleDAO
    abstract fun castingDAO(): CastingDAO
    abstract fun crewDAO(): CrewDAO
    abstract fun productionCompanyDao(): ProductionCompanyDao
    abstract fun movieToProductionDao(): MovieToProductionDao

    override fun clearAllTables() {
        //todo see what to implement here
    }

    companion object {
        private const val NAME_DATABASE = "demo_movie_wes.db"

        @Volatile
        private var sInstance: appDatabase? = null

        fun getInstance(context: Context): appDatabase {
            sInstance ?: synchronized(this) {
                sInstance ?: buildDatabase(context).also {
                    sInstance = it
                }
            }

            return sInstance!!
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                appDatabase::class.java,
                NAME_DATABASE
            )
                .fallbackToDestructiveMigration()
                .build()

    }
}