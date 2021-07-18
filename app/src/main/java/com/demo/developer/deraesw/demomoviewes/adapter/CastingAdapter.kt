package com.demo.developer.deraesw.demomoviewes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.developer.deraesw.demomoviewes.core.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.databinding.ItemCastingItemBinding

class CastingAdapter: ListAdapter<CastingItem, RecyclerView.ViewHolder>(CastingDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as CastingViewHolder).bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastingViewHolder {
        val binding = ItemCastingItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return CastingViewHolder(binding)
    }

    inner class CastingViewHolder(var binding:ItemCastingItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: CastingItem) {
            binding.apply {
                casting = item
                executePendingBindings()
            }
        }
    }
}

private class CastingDiffCallback: DiffUtil.ItemCallback<CastingItem>() {

    override fun areItemsTheSame(oldItem: CastingItem, newItem: CastingItem): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CastingItem, newItem: CastingItem): Boolean {
        return  oldItem == newItem
    }
}