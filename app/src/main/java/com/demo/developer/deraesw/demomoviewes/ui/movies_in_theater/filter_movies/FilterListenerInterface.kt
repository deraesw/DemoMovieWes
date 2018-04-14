package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies

interface FilterListenerInterface {
    fun onFilterChange(list: List<Int>)
    fun clearFilter()
}