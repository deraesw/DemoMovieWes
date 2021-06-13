package com.demo.developer.deraesw.demomoviewes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.databinding.ItemFilterMovieBinding

class FilterMovieAdapter(val handler: FilterMovieAdapterInterface)
    : ListAdapter<GenreFilter, RecyclerView.ViewHolder>(FilterMovieDiffCallback()) {
    interface FilterMovieAdapterInterface {

        fun clickOnItem(id : Int, checked: Boolean)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as FilterMovieViewHolder).bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMovieViewHolder {
        val binding = ItemFilterMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return FilterMovieViewHolder(binding)
    }

    inner class FilterMovieViewHolder(var binding : ItemFilterMovieBinding ) :
            RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val checkStatus = !(binding.cbGenre.isChecked)
            binding.cbGenre.isChecked = checkStatus
            handler.clickOnItem(getItem(adapterPosition).id, checkStatus)
        }

        fun bind(item : GenreFilter) {
            binding.apply {
                genre = item
                cbGenre.setOnClickListener {
                    handler.clickOnItem(item.id, cbGenre.isChecked)
                }
                executePendingBindings()
            }
        }
    }
}

private class FilterMovieDiffCallback: DiffUtil.ItemCallback<GenreFilter>() {

    override fun areItemsTheSame(oldItem: GenreFilter, newItem: GenreFilter): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GenreFilter, newItem: GenreFilter): Boolean {
        return  oldItem == newItem
    }
}