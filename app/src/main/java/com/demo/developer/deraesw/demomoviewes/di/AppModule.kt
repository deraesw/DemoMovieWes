package com.demo.developer.deraesw.demomoviewes.di

import android.content.Context
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.AppDatabase
import com.demo.developer.deraesw.demomoviewes.data.dao.CastingDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.MovieGenreDAO
import com.demo.developer.deraesw.demomoviewes.data.dao.PeopleDAO
import com.demo.developer.deraesw.demomoviewes.network.MoviedbAPI
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideAppDataSource(appDatabase: AppDatabase, appExecutors: AppExecutors): AppDataSource {
        return AppDataSource.getInstance(appDatabase, appExecutors)
    }

    @Singleton
    @Provides
    fun provideCastingDAO(appDatabase: AppDatabase): CastingDAO {
        return appDatabase.castingDAO()
    }

    @Singleton
    @Provides
    fun providePeopleDAO(appDatabase: AppDatabase): PeopleDAO {
        return appDatabase.peopleDAO()
    }

    @Singleton
    @Provides
    fun provideMovieGenreDAO(appDatabase: AppDatabase): MovieGenreDAO {
        return appDatabase.movieGenreDao()
    }


//    @Singleton
//    @Provides
//    fun provideMovieGenreRepository(
//        movieGenreCallHandler: MovieGenreCallHandler,
//        appDataSource: AppDataSource,
//        appExecutors: AppExecutors
//    ): MovieGenreRepository {
//        return MovieGenreRepository(movieGenreCallHandler, appDataSource, appExecutors)
//    }

    @Provides
    fun provideAppExecutors(): AppExecutors {
        return AppExecutors.getInstance()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.MOVIE_API_WEB)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit) : MoviedbAPI {
        return retrofit.create(MoviedbAPI::class.java)
    }
}