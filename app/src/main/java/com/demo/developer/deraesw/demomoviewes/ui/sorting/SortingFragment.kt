package com.demo.developer.deraesw.demomoviewes.ui.sorting

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.SortingAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.SortItem
import com.demo.developer.deraesw.demomoviewes.utils.Constant


class SortingFragment : Fragment(), SortingAdapter.SortingMovieAdapterInterface{

    private val TAG = SortingFragment::class.java.simpleName

    companion object {
        const val KEY_SORT_CATEGORY = "KEY_SORT_CATEGORY"
        const val KEY_SORT_CODE = "KEY_SORT_CODE"

        fun setup(categorySorting : String,  codeKey : String) : Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_SORT_CODE, codeKey)
            bundle.putString(KEY_SORT_CATEGORY, categorySorting)
            return bundle
        }
    }

    class Category {
        companion object {
            const val SORT_MOVIE   = "SORT_MOVIE"
            const val SORT_CASTING = "SORT_CASTING"
            const val SORT_CREW    = "SORT_CREW"
        }
    }

    interface SortingFragmentInterface {
        fun selectSortingOption(code : String)
    }

    private lateinit var mAdapter : SortingAdapter
    private lateinit var mCode : String
    private lateinit var mCategory: String
    private lateinit var mHandle : SortingFragmentInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCategory = arguments?.getString(KEY_SORT_CATEGORY) ?: ""
        mCode = arguments?.getString(KEY_SORT_CODE) ?: Constant.SortingCode.BY_DEFAULT
        if(mCode == ""){
            mCode = Constant.SortingCode.BY_DEFAULT
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_sorting, container, false)

        val recyclerView : RecyclerView = viewRoot.findViewById(R.id.rv_sorting_options)
        //recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        mAdapter = SortingAdapter(this)
        recyclerView.adapter = mAdapter

        var sortingOptionCode = HashMap<Int, String>()
        var sortingOptionLabel = arrayOf<String>()

        when(mCategory){
            Category.SORT_MOVIE -> {
                sortingOptionCode = Constant.SortingCode.Movie.CODE
                sortingOptionLabel = resources.getStringArray(R.array.movie_sorting_label)
            }
            Category.SORT_CASTING -> {
                sortingOptionCode = Constant.SortingCode.Casting.CODE
                sortingOptionLabel = resources.getStringArray(R.array.casting_sorting_label)
            }
            Category.SORT_CREW -> {

            }
        }

        var movieSortList : List<SortItem> = ArrayList()
        sortingOptionCode.toSortedMap().forEach{
            index, value -> movieSortList += SortItem(
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
            mHandle = context as SortingFragmentInterface
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
