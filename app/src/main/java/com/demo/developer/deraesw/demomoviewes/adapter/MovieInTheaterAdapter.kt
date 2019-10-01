package com.demo.developer.deraesw.demomoviewes.adapter


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieInTheaterBinding

class MovieInTheaterAdapter(val mHandler: MovieInTheaterAdapterInterface)
    : ListAdapter<MovieInTheater, RecyclerView.ViewHolder>(MovieInTheaterDiffCallback()) {

    interface MovieInTheaterAdapterInterface {
        fun clickOnItem(id : Int, view: View)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieInTheater = getItem(position)
        (holder as MovieInTheaterViewHolder).bind(movieInTheater)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieInTheaterViewHolder {
        return MovieInTheaterViewHolder(ItemMovieInTheaterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    inner class MovieInTheaterViewHolder(private val binding:ItemMovieInTheaterBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: MovieInTheater) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            mHandler.clickOnItem(getItem(position).id, binding.incContent.ivMoviePoster)
        }
    }
}

private class MovieInTheaterDiffCallback: DiffUtil.ItemCallback<MovieInTheater>() {

    override fun areItemsTheSame(oldItem: MovieInTheater, newItem: MovieInTheater): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieInTheater, newItem: MovieInTheater): Boolean {
        return  oldItem == newItem
    }
}