package com.demo.developer.deraesw.demomoviewes.ui

interface NavigationInterface {
    fun clickOnLaunchMovieDetailView(key : Int)

    companion object {
        const val RC_MOVIE_SORTING_OPTION = 1000
    }
}