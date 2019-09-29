package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.ProductionCompany
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieProductionBinding

class ProductionAdapter: ListAdapter<ProductionCompany, RecyclerView.ViewHolder>(ProductionDiffCallback()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ProductionViewHolder).bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductionViewHolder {
        val binding = ItemMovieProductionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return ProductionViewHolder(binding)
    }


    inner class ProductionViewHolder(private val binding:ItemMovieProductionBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: ProductionCompany) {
            binding.apply {
                prod = item
                executePendingBindings()
            }
        }
    }
}

private class ProductionDiffCallback: DiffUtil.ItemCallback<ProductionCompany>() {

    override fun areItemsTheSame(oldItem: ProductionCompany, newItem: ProductionCompany): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductionCompany, newItem: ProductionCompany): Boolean {
        return  oldItem == newItem
    }
}