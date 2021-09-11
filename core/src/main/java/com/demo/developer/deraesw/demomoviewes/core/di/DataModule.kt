package com.demo.developer.deraesw.demomoviewes.core.di

import android.content.Context
import com.demo.developer.deraesw.demomoviewes.core.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.core.data.AppDatabase
import com.demo.developer.deraesw.demomoviewes.core.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideAppDataSource(appDatabase: AppDatabase): AppDataSource {
        return AppDataSource.getInstance(appDatabase)
    }

    @Singleton
    @Provides
    fun provideCastingDAO(appDatabase: AppDatabase): CastingDAO {
        return appDatabase.castingDAO()
    }

    @Singleton
    @Provides
    fun provideCrewDAO(appDatabase: AppDatabase): CrewDAO {
        return appDatabase.crewDAO()
    }

    @Singleton
    @Provides
    fun provideMovieDAO(appDatabase: AppDatabase): MovieDAO {
        return appDatabase.movieDAO()
    }

    @Singleton
    @Provides
    fun provideMovieGenreDAO(appDatabase: AppDatabase): MovieGenreDAO {
        return appDatabase.movieGenreDao()
    }

    @Singleton
    @Provides
    fun provideMovieToGenreDAO(appDatabase: AppDatabase): MovieToGenreDAO {
        return appDatabase.movieToGenreDAO()
    }

    @Singleton
    @Provides
    fun provideMovieToProductionDao(appDatabase: AppDatabase): MovieToProductionDao {
        return appDatabase.movieToProductionDao()
    }

    @Singleton
    @Provides
    fun providePeopleDAO(appDatabase: AppDatabase): PeopleDAO {
        return appDatabase.peopleDAO()
    }

    @Singleton
    @Provides
    fun provideProductionCompanyDao(appDatabase: AppDatabase): ProductionCompanyDao {
        return appDatabase.productionCompanyDao()
    }
}