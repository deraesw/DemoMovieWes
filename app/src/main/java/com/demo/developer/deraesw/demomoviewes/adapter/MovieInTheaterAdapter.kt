package com.demo.developer.deraesw.demomoviewes.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieInTheaterBinding

class MovieInTheaterAdapter : RecyclerView.Adapter<MovieInTheaterAdapter.MovieInTheaterViewHolder>() {
    private val TAG = MovieInTheaterAdapter::class.java.simpleName

    private var mList: List<MovieInTheater> = ArrayList();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieInTheaterViewHolder {
        val binding = ItemMovieInTheaterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return MovieInTheaterViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MovieInTheaterViewHolder, position: Int) {
        val movieInTheater = mList.get(position)
        holder.binding?.movie = movieInTheater

        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = mList.size

    fun swapData(list: List<MovieInTheater>){
        if(mList.isEmpty()){
            mList = list
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = mList.size

                override fun getNewListSize(): Int = list.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mList.get(oldItemPosition).id == list.get(newItemPosition).id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val isContentSame =
                            mList.get(oldItemPosition).equals(list.get(newItemPosition))
                    return isContentSame
                }
            })
            mList = list
            result.dispatchUpdatesTo(this)
        }
    }

    inner class MovieInTheaterViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        internal var binding:ItemMovieInTheaterBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }
    }
}