package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.sorting_movies

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.SortingMovieAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.MovieSortItem
import com.demo.developer.deraesw.demomoviewes.utils.Constant

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SortingMovieFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SortingMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SortingMovieFragment : Fragment(), SortingMovieAdapter.SortingMovieAdapterInterface{

    private val TAG = SortingMovieFragment::class.java.simpleName

    companion object {
        const val KEY_SORT_CODE = "KEY_SORT_CODE"

        fun setup(codeKey : String) : Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_SORT_CODE, codeKey)
            return bundle
        }
    }

    interface SortingMovieFragmentInterface {
        fun selectSortingOption(code : String)
    }

    private lateinit var mAdapter : SortingMovieAdapter
    private lateinit var mCode : String
    private lateinit var mHandle : SortingMovieFragmentInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCode = arguments?.getString(KEY_SORT_CODE) ?: Constant.SortingCode.BY_DEFAULT
        if(mCode == ""){
            mCode = Constant.SortingCode.BY_DEFAULT
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_sorting_movie, container, false)

        val recyclerView : RecyclerView = viewRoot.findViewById(R.id.rv_sorting_options)
        //recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        mAdapter = SortingMovieAdapter(this)
        recyclerView.adapter = mAdapter

        val sortingOptionCode = Constant.SortingCode.CODE
        val sortingOptionLabel = resources.getStringArray(R.array.movie_sorting_label)

        var movieSortList : List<MovieSortItem> = ArrayList()
        sortingOptionCode.forEach{
            index, value -> movieSortList += MovieSortItem(
                value,
                sortingOptionLabel.get(index) ?: "",
                (value == mCode)
                )
        }

        mAdapter.swapData(movieSortList)

        return viewRoot
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mHandle = context as SortingMovieFragmentInterface
        } catch (ex : Exception){
            Log.e(TAG, "Must implement SortingMovieFragmentInterface")
        }

    }

    override fun clickOnItem(position: Int) {
        mAdapter.changeSelectedItem(position)
        mCode = mAdapter.getItemAt(position).key
        mHandle.selectSortingOption(mCode)
    }
}
