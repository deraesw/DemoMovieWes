package com.demo.developer.deraesw.demomoviewes.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.DummyMainActivityViewModel
import com.demo.developer.deraesw.demomoviewes.ui.MainActivityViewModel
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailViewModel
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section.MovieCastingViewModel
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section.MovieCrewViewModel
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterViewModel
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterMovieViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelBuilder {

    @Binds
    internal abstract fun bindViewModelFactory(factory : DemoViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DummyMainActivityViewModel::class)
    abstract fun bindDummyViewModel(viewModel : DummyMainActivityViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel : MainActivityViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoviesInTheaterViewModel::class)
    abstract fun bindMoviesInTheaterViewModel(viewModel : MoviesInTheaterViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterMovieViewModel::class)
    abstract fun bindFilterMovieViewModel(viewModel : FilterMovieViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailViewModel(viewModel : MovieDetailViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieCastingViewModel::class)
    abstract fun bindMovieCastingViewModel(viewModel : MovieCastingViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieCrewViewModel::class)
    abstract fun bindMovieCrewViewModel(viewModel : MovieCrewViewModel) : ViewModel
}