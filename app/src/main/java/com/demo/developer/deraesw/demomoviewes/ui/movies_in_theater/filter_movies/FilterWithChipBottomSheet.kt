package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.FilterMovieAdapter
import com.demo.developer.deraesw.demomoviewes.core.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.databinding.WidgetChipBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup

class FilterWithChipBottomSheet(
    val list: List<GenreFilter>,
    private val handler: FilterMovieAdapter.FilterMovieAdapterInterface,
    private val filterHandler: FilterListenerInterface
) : BottomSheetDialogFragment(), FilterMovieAdapter.FilterMovieAdapterInterface {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot =
            inflater.inflate(R.layout.fragment_filter_movies_with_chips, container, false)

        val chipGroup: ChipGroup = viewRoot.findViewById(R.id.cg_filters_group)

        list.forEach { genreFilter ->
            val widgetChip = WidgetChipBinding.inflate(layoutInflater)
            widgetChip.genreFilter = genreFilter
            widgetChip.chip.setOnClickListener {
                genreFilter.checked = !genreFilter.checked
                handler.clickOnItem(genreFilter.id, !genreFilter.checked)
            }

            chipGroup.addView(widgetChip.root)
        }


        viewRoot.findViewById<ImageView>(R.id.iv_clear_all_filter).apply {
            setOnClickListener {
                filterHandler.clearFilter()
                this@FilterWithChipBottomSheet.dismiss()
            }
        }

        return viewRoot
    }

    override fun clickOnItem(id: Int, checked: Boolean) {
        handler.clickOnItem(id, checked)
    }
}