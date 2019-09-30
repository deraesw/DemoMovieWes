package com.demo.developer.deraesw.demomoviewes.di

import com.demo.developer.deraesw.demomoviewes.ui.home.HomeFragment
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailActivityFragment
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailCreditsFragment
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailReviewFragment
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section.MovieCastingFragment
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section.MovieCrewFragment
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterFragment
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterMoviesFragment
import com.demo.developer.deraesw.demomoviewes.ui.synchronize_data.SynchronizedDataActivityFragment
import com.demo.developer.deraesw.demomoviewes.ui.upcoming.UpcomingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMoviesInTheaterFragment() : MoviesInTheaterFragment

    @ContributesAndroidInjector
    internal abstract fun contributeFilterMoviesFragment() : FilterMoviesFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMovieDetailActivityFragment() : MovieDetailActivityFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMovieDetailCreditsFragment() : MovieDetailCreditsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMovieCastingFragment() : MovieCastingFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMovieCrewFragment() : MovieCrewFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMovieDetailReviewFragment() : MovieDetailReviewFragment

    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragment() : HomeFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSynchronizedDataActivityFragment() : SynchronizedDataActivityFragment

    @ContributesAndroidInjector
    internal abstract fun contributeUpcomingFragment() : UpcomingFragment
}