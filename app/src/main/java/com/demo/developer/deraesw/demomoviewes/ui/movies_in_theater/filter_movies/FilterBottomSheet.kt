package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.FilterMovieAdapter
import com.demo.developer.deraesw.demomoviewes.core.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet(
        val list: List<GenreFilter>,
        private val handler: FilterMovieAdapter.FilterMovieAdapterInterface,
        private val filterHandler: FilterListenerInterface)
    : BottomSheetDialogFragment()
        , FilterMovieAdapter.FilterMovieAdapterInterface {

    private lateinit var adapter: FilterMovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_filter_movies, container, false)

        adapter = FilterMovieAdapter(this)

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_filter_option)
        recyclerView.apply {
            setLinearLayout()
            adapter = this@FilterBottomSheet.adapter
        }

        viewRoot.findViewById<ImageView>(R.id.iv_clear_all_filter).apply {
            setOnClickListener{
                filterHandler.clearFilter()
                this@FilterBottomSheet.dismiss()
            }
        }

        adapter.submitList(list)

        return viewRoot
    }

    override fun clickOnItem(id: Int, checked: Boolean) {
        handler.clickOnItem(id, checked)
    }
}