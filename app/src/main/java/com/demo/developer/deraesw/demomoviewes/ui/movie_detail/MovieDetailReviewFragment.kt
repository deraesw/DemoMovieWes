package com.demo.developer.deraesw.demomoviewes.ui.movie_detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.demo.developer.deraesw.demomoviewes.R


/**
 * A simple [Fragment] subclass.
 *
 */
class MovieDetailReviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_detail_review, container, false)

        val toolbar = viewRoot.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(true)
                setDisplayHomeAsUpEnabled(true)
                title = "test"
            }
        }
        return viewRoot
    }


}
