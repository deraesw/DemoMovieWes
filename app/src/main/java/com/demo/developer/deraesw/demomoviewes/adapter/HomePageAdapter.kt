package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterFragment
import com.demo.developer.deraesw.demomoviewes.ui.upcoming.UpcomingFragment


class HomePageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment)  {

    companion object {
        const val NOW_PLAYING_PAGE_INDEX = 0
        const val UPCOMING_PAGE_INDEX = 1
    }

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
            NOW_PLAYING_PAGE_INDEX to { MoviesInTheaterFragment() },
            UPCOMING_PAGE_INDEX to { UpcomingFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.count()

    override fun createFragment(position: Int) = tabFragmentsCreators[position]?.invoke()
            ?: throw IndexOutOfBoundsException()
}