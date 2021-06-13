package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Singleton
class MovieGenreRepositoryTest() : MovieGenreRepositoryInterface {
    override val mMovieGenreList: Flow<List<MovieGenre>> = flowOf()
}