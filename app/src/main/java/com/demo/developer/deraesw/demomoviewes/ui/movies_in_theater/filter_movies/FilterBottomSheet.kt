package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.FilterMovieAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet(): BottomSheetDialogFragment() {

    private lateinit var adapter : FilterMovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_filter_movies, container, false)



        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_filter_option)
        recyclerView.apply {
            setLinearLayout()
            //adapter = this@FilterBottomSheet.adapter
        }

        return viewRoot
    }
}