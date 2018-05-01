package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.CastingAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.ui.NavigationInterface
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingActivity
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingFragment
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import dagger.android.support.DaggerFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class MovieCastingFragment : DaggerFragment() {

    private val TAG = MovieCastingFragment::class.java.simpleName

    @Inject
    lateinit var mFactory : ViewModelProvider.Factory
    private lateinit var mViewModel : MovieCastingViewModel
    private lateinit var mAdapter : CastingAdapter
    private lateinit var mEmptyView : View

    private var mMovieId : Int = 0
    private var sortingByCode : String = Constant.SortingCode.BY_DEFAULT
    private var originalList : List<CastingItem> = ArrayList()

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_casting, container, false)

        mMovieId = arguments?.getInt(KEY_MOVIE_ID) ?: 0

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_casting_list)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)

        mAdapter = CastingAdapter()
        recyclerView.adapter = mAdapter

        mEmptyView = viewRoot.findViewById(R.id.inc_empty_list)

        val swipeRefreshLayout = viewRoot.findViewById<SwipeRefreshLayout>(R.id.sf_casting_list)

        if(mMovieId != 0){
            mViewModel = ViewModelProviders.of(this, mFactory).get(MovieCastingViewModel::class.java)

            mViewModel.getMovieCasting(mMovieId).observe(this, Observer {
                if(it != null){
                    if(it.isNotEmpty()){
                        originalList = it
                        manageItems()
                    } else {
                        mViewModel.fetchMovieCredits(mMovieId)
                        swipeRefreshLayout.isRefreshing = true
                    }

                } else {
                    mViewModel.fetchMovieCredits(mMovieId)
                    swipeRefreshLayout.isRefreshing = true
                }

                if(swipeRefreshLayout.isRefreshing){
                    swipeRefreshLayout.isRefreshing = false
                }

                manageDisplayEmptyView()
            })

            swipeRefreshLayout.setOnRefreshListener({
                mViewModel.fetchMovieCredits(mMovieId)
            })
        }

        return viewRoot
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_movie_detail_casting, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.action_sort_content -> {
                launchSortingActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            NavigationInterface.RC_CASTING_SORTING_OPTION -> {
                if(resultCode == Activity.RESULT_OK ){
                    sortingByCode =
                            data?.getStringExtra(SortingActivity.KEY_NEW_CODE_SELECTED) ?:
                            Constant.SortingCode.BY_DEFAULT
                    manageItems()
                }
            }
        }
    }

    private fun manageItems(){
        var filterList = originalList

        filterList = when(sortingByCode){
            Constant.SortingCode.BY_DEFAULT -> filterList.sortedBy { it.id }
            Constant.SortingCode.Casting.BY_NAME_ASC -> filterList.sortedBy { it.name }
            Constant.SortingCode.Casting.BY_NAME_DESC -> filterList.sortedByDescending { it.name }
            Constant.SortingCode.Casting.BY_CHARACTER_NAME_ASC -> filterList.sortedBy { it.character }
            Constant.SortingCode.Casting.BY_CHARACTER_NAME_DESC -> filterList.sortedByDescending { it.character }
            else -> filterList.sortedBy { it.id }
        }

        mAdapter.swapData(filterList)
    }

    private fun manageDisplayEmptyView(){
        mEmptyView.visibility = if(originalList.isNotEmpty()) View.GONE else View.VISIBLE
    }

    private fun launchSortingActivity(){
        val intent = Intent(context, SortingActivity::class.java)
        intent.putExtra(SortingActivity.KEY_SORT_CATEGORY, SortingFragment.Category.SORT_CASTING)
        intent.putExtra(SortingActivity.KEY_CODE_SELECTED, sortingByCode)
        startActivityForResult(intent, NavigationInterface.RC_CASTING_SORTING_OPTION)
    }
}
