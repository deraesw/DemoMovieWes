package com.demo.developer.deraesw.demomoviewes.di

import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepositoryInterface
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepositoryTest
import com.demo.developer.deraesw.demomoviewes.repository.SyncRepositoryInterface
import com.demo.developer.deraesw.demomoviewes.repository.SyncRepositoryTest
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SyncRepositoryModule::class]
)
class TestModule {

    @Singleton
    @Provides
    fun provideSyncRepository(): SyncRepositoryInterface = SyncRepositoryTest()

    @Singleton
    @Provides
    fun provideMovieGenreRepository(): MovieGenreRepositoryInterface = MovieGenreRepositoryTest()
}
