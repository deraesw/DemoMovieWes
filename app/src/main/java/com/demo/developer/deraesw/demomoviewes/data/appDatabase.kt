package com.demo.developer.deraesw.demomoviewes.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre

@Database(entities = arrayOf(MovieGenre::class), version = 1)
abstract class appDatabase : RoomDatabase(){
    private val TAG = appDatabase::class.java.simpleName

    abstract fun movieGenreDao() : MovieGenreDAO

    companion object {
        const val NAME_DATABASE = "demo_movie_wes.db"

        @Volatile private var sInstance : appDatabase? = null

        fun getInstance(context: Context) : appDatabase {
            sInstance ?: synchronized(this){
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
                        appDatabase.NAME_DATABASE)
                        .fallbackToDestructiveMigration()
                        .build()

    }
}