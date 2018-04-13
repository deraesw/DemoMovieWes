package com.demo.developer.deraesw.demomoviewes.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.ItemFilterMovieBinding
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

class FilterMovieAdapter(val mHandler: FilterMovieAdapterInterface): RecyclerView.Adapter<FilterMovieAdapter.FilterMovieViewHolder>() {
    private val TAG = FilterMovieAdapter::class.java.simpleName

    private var mList: List<MovieGenre> = ArrayList();

    interface FilterMovieAdapterInterface {
        fun clickOnItem(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMovieViewHolder {
        val binding = ItemFilterMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return FilterMovieViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FilterMovieViewHolder, position: Int) {
        val genre = mList.get(position)
        holder.binding?.genre = genre
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = mList.size

    fun getItemAt(position : Int) : MovieGenre = mList.get(position)

    fun swapData(list: List<MovieGenre>){
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

    inner class FilterMovieViewHolder(itemView : View) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener{

        internal var binding : ItemFilterMovieBinding ? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.d(TAG, "Click on item")
            binding!!.cbGenre.isChecked = !(binding!!.cbGenre.isChecked)
        }
    }
}