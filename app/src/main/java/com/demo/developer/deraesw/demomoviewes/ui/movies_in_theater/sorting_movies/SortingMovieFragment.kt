package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.sorting_movies

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.SortingAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.SortItem
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
@Deprecated("to remove, use SortingActivity instead")
class SortingMovieFragment : androidx.fragment.app.Fragment(), SortingAdapter.SortingMovieAdapterInterface{

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

    private lateinit var mAdapter : SortingAdapter
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

        val recyclerView : androidx.recyclerview.widget.RecyclerView = viewRoot.findViewById(R.id.rv_sorting_options)
        //recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        mAdapter = SortingAdapter(this)
        recyclerView.adapter = mAdapter

        val sortingOptionCode = Constant.SortingCode.CODE
        val sortingOptionLabel = resources.getStringArray(R.array.movie_sorting_label)

        var movieSortList : List<SortItem> = ArrayList()
        sortingOptionCode.forEach{
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
