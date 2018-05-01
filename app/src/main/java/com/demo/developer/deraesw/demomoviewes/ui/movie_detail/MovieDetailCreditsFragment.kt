package com.demo.developer.deraesw.demomoviewes.ui.movie_detail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.ViewPagerAdapter
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section.MovieCastingFragment
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section.MovieCrewFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MovieDetailCreditsFragment : DaggerFragment() {

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }
    }

    private val TAG = MovieDetailCreditsFragment::class.java.simpleName

    @Inject
    lateinit var mFactory : ViewModelProvider.Factory
    private lateinit var mViewModel : MovieDetailViewModel
    private var mMovieId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_credits, container, false)

        val toolbar = viewRoot.findViewById<Toolbar>(R.id.toolbar)
        val tableLayout = viewRoot.findViewById<TabLayout>(R.id.table_layout)
        val viewPage = viewRoot.findViewById<ViewPager>(R.id.viewpager)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
        }

        mMovieId = arguments?.getInt(KEY_MOVIE_ID) ?: 0

        if(mMovieId != 0){
           mViewModel = ViewModelProviders.of(this, mFactory).get(MovieDetailViewModel::class.java)

            mViewModel.getMovieDetail(mMovieId).observe(this, Observer {
                if(it != null){
                    (activity as AppCompatActivity).supportActionBar?.title = it.title
                }
            })

            val viewPagerAdapter = ViewPagerAdapter(fragmentManager!!)

            val castFragment = MovieCastingFragment()
            castFragment.arguments = MovieCastingFragment.setupBundle(mMovieId)
            viewPagerAdapter.addFragment(castFragment, getString(R.string.tab_casting))

            val crewFragment = MovieCrewFragment()
            crewFragment.arguments = MovieCrewFragment.setupBundle(mMovieId)
            viewPagerAdapter.addFragment(crewFragment, getString(R.string.tab_crew))

            viewPage.adapter = viewPagerAdapter
            tableLayout.setupWithViewPager(viewPage)
        }

        return viewRoot
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }


}
