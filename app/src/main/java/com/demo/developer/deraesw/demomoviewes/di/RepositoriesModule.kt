package com.demo.developer.deraesw.demomoviewes.di

import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SyncRepositoryModule {

    @Singleton
    @Provides
    fun provideSyncRepository(
        genreRepository: MovieGenreRepository,
        sharePrefRepository: SharePrefRepository,
        movieCreditsRepository: MovieCreditsRepository,
        movieRepository: MovieRepository
    ): SyncRepositoryInterface = SyncRepository(
        genreRepository,
        sharePrefRepository,
        movieCreditsRepository,
        movieRepository
    )

    @Singleton
    @Provides
    fun provideMovieGenreRepository(
        networkRepository: NetworkRepository,
        movieGenreDAO: MovieGenreDAO
    ): MovieGenreRepositoryInterface = MovieGenreRepository(
        networkRepository, movieGenreDAO
    )
}