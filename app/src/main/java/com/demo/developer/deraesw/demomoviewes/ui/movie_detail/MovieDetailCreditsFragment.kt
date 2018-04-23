package com.demo.developer.deraesw.demomoviewes.ui.movie_detail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.ViewPagerAdapter
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section.MovieCastingFragment
import com.demo.developer.deraesw.demomoviewes.utils.Injection


class MovieDetailCreditsFragment : Fragment() {
    private val TAG = MovieDetailCreditsFragment::class.java.simpleName

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
            val factory = Injection.provideMovieDetailFactory(context!!, mMovieId)
            mViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel::class.java)

            mViewModel.movie.observe(this, Observer {
                if(it != null){
                    (activity as AppCompatActivity).supportActionBar?.title = it.title
                }
            })

            val viewPagerAdapter = ViewPagerAdapter(fragmentManager!!)

            val castFragment = MovieCastingFragment()
            castFragment.arguments = MovieCastingFragment.setupBundle(mMovieId)
            viewPagerAdapter.addFragment(castFragment, getString(R.string.tab_casting))

            viewPage.adapter = viewPagerAdapter
            tableLayout.setupWithViewPager(viewPage)
        }



        return viewRoot
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //todo remove, only for testing
        menu?.add(Menu.NONE,9999, Menu.NONE, "Test fetch data")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            9999 -> {
                val test = Injection.provideMovieCreditsRepository(context!!)
                test.fetchMovieCredits(mMovieId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }
    }
}
