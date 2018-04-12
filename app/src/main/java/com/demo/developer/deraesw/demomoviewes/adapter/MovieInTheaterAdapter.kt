package com.demo.developer.deraesw.demomoviewes.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

class MovieInTheaterAdapter(val mHandler: MovieInTheaterAdapterInterface): RecyclerView.Adapter<MovieInTheaterAdapter.MovieInTheaterViewHolder>() {
    private val TAG = MovieInTheaterAdapter::class.java.simpleName

    private var mList: List<MovieInTheater> = ArrayList();

    interface MovieInTheaterAdapterInterface {
        fun clickOnItem(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieInTheaterViewHolder {
        val binding = ItemMovieInTheaterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return MovieInTheaterViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MovieInTheaterViewHolder, position: Int) {
        val movieInTheater = mList.get(position)
        val content = holder.binding?.incContent

        holder.binding?.movie = movieInTheater
        content?.ivMoviePoster?.setImageUrl(
                movieInTheater.posterPath,
                AppTools.PosterSize.SMALL)

        if(movieInTheater.releaseDate != null){
            content?.tvMovieReleaseDate?.text = AppTools.convertDateString(movieInTheater.releaseDate!!, AppTools.DatePattern.MM_DD_YYY_S_PATTERN)
        }

        val genreListName = movieInTheater.genres.joinToString (transform = {it.name})
        content?.tvMovieGenre?.text = genreListName

        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = mList.size

    fun getItemAt(position : Int) : MovieInTheater = mList.get(position)

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

    inner class MovieInTheaterViewHolder(itemView : View) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener{

        internal var binding:ItemMovieInTheaterBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.d(TAG, "Click on item")
            val position = adapterPosition;
            mHandler.clickOnItem(position)
        }
    }
}