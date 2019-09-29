package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section


import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.navigation.fragment.navArgs
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.CastingAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
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

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }
    }

    @Inject lateinit var mFactory : ViewModelProvider.Factory
    private lateinit var mViewModel : MovieCastingViewModel
    private lateinit var mAdapter : CastingAdapter
    private lateinit var mEmptyView : View
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private val args: MovieCastingFragmentArgs by navArgs()
    private var mMovieId : Int = 0
    private var mSortingCode : String = Constant.SortingCode.BY_DEFAULT
    private var mOriginalList : List<CastingItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_casting, container, false)

        mMovieId = args.EXTRAMOVIEID

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_casting_list)
        recyclerView.setLinearLayout()

        mAdapter = CastingAdapter()
        recyclerView.adapter = mAdapter

        mEmptyView = viewRoot.findViewById(R.id.inc_empty_list)

        mSwipeRefreshLayout = viewRoot.findViewById(R.id.sf_casting_list)

        mSwipeRefreshLayout.setOnRefreshListener(this@MovieCastingFragment::fetchMovieCredits)

        return viewRoot
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(mMovieId != 0){
            mViewModel = ViewModelProviders.of(this, mFactory).get(MovieCastingViewModel::class.java)

            mViewModel.getMovieCasting(mMovieId).observe(this, Observer {
                if(it != null){
                    if(it.isNotEmpty()){
                        mOriginalList = it
                        manageItems()
                    }
                }

                if(mSwipeRefreshLayout.isRefreshing){
                    mSwipeRefreshLayout.isRefreshing = false
                }

                manageDisplayEmptyView()
            })

            mViewModel.errorNetwork.observe(this, Observer {
                if(it != null){
                    mSwipeRefreshLayout.isRefreshing = false
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_movie_detail_casting, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
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
                    mSortingCode =
                            data?.getStringExtra(SortingActivity.EXTRA_NEW_CODE_SELECTED) ?:
                            Constant.SortingCode.BY_DEFAULT
                    manageItems()
                }
            }
        }
    }

    private fun fetchMovieCredits() {
        mViewModel.fetchMovieCredits(mMovieId)
    }

    private fun manageItems(){
        var filterList = mOriginalList

        filterList = when(mSortingCode){
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
        mEmptyView.visibility = if(mOriginalList.isNotEmpty()) View.GONE else View.VISIBLE
    }

    private fun launchSortingActivity(){
        val intent = SortingActivity.setup(
                context!!,
                SortingFragment.Category.SORT_CASTING,
                mSortingCode
        )
        startActivityForResult(intent, NavigationInterface.RC_CASTING_SORTING_OPTION)
    }
}
