package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieDetailCastingItemBinding

class MovieDetailCastingAdapter(val handler : MovieDetailCastingAdapterInterface)
    : ListAdapter<CastingItem, RecyclerView.ViewHolder>(MovieDetailCastingDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item= getItem(position)
        (holder as MovieDetailCastingViewHolder).bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailCastingViewHolder {
        val binding = ItemMovieDetailCastingItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return MovieDetailCastingViewHolder(binding)
    }

    inner class MovieDetailCastingViewHolder(private val binding:ItemMovieDetailCastingItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        fun bind(item: CastingItem) {
            binding.apply {
                casting = item
                if(item.specialItemAction) {
                    root.setOnClickListener(this@MovieDetailCastingViewHolder)
                }
                executePendingBindings()
            }
        }

        override fun onClick(v: View?) {
            handler.clickOnMoreAction()
        }
    }

    interface MovieDetailCastingAdapterInterface {
        fun clickOnMoreAction()
    }
}

private class MovieDetailCastingDiffCallback: DiffUtil.ItemCallback<CastingItem>() {

    override fun areItemsTheSame(oldItem: CastingItem, newItem: CastingItem): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CastingItem, newItem: CastingItem): Boolean {
        return  oldItem == newItem
    }
}