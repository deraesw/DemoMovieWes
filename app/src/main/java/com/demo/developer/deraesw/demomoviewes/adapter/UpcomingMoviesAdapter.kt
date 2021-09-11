package com.demo.developer.deraesw.demomoviewes.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.developer.deraesw.demomoviewes.core.data.model.UpcomingMovie
import com.demo.developer.deraesw.demomoviewes.databinding.ItemUpcomingMovieBinding

class UpcomingMoviesAdapter(val handler: UpcomingMovieAdapterInterface)
    : ListAdapter<UpcomingMovie, RecyclerView.ViewHolder>(UpcomingMoviesDiffCallback()) {

    interface UpcomingMovieAdapterInterface {
        fun clickOnItem(id : Int)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieInTheater = getItem(position)
        (holder as UpcomingMovieViewHolder).bind(movieInTheater)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMovieViewHolder {
        return UpcomingMovieViewHolder(ItemUpcomingMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    inner class UpcomingMovieViewHolder(private val binding:ItemUpcomingMovieBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: UpcomingMovie) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            handler.clickOnItem(getItem(position).id)
        }
    }
}

private class UpcomingMoviesDiffCallback: DiffUtil.ItemCallback<UpcomingMovie>() {

    override fun areItemsTheSame(oldItem: UpcomingMovie, newItem: UpcomingMovie): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UpcomingMovie, newItem: UpcomingMovie): Boolean {
        return  oldItem == newItem
    }
}