package com.demo.developer.deraesw.demomoviewes.repository

class MainRepository private constructor(val genreRepository: MovieGenreRepository, val sharePrefRepository: SharePrefRepository){

    companion object {
        @Volatile private var sInstance : MainRepository? = null

        fun getInstance(
                genreRepository: MovieGenreRepository,
                sharePrefRepository: SharePrefRepository) : MainRepository {
            sInstance ?: synchronized(this){
                sInstance = MainRepository(genreRepository, sharePrefRepository)
            }

            return sInstance!!
        }
    }
}