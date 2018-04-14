package com.demo.developer.deraesw.demomoviewes.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.ItemFilterMovieBinding
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

class FilterMovieAdapter(val mHandler: FilterMovieAdapterInterface): RecyclerView.Adapter<FilterMovieAdapter.FilterMovieViewHolder>() {
    private val TAG = FilterMovieAdapter::class.java.simpleName

    private var mList: List<GenreFilter> = ArrayList()

    interface FilterMovieAdapterInterface {
        fun clickOnItem(id : Int, checked: Boolean)
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

    fun getItemAt(position : Int) : GenreFilter = mList.get(position)

    fun unSelectAllFilter(){
        mList.forEach({
            it.checked = false
        })
        notifyDataSetChanged()
    }

    fun isAtLeastOneItemSelected() : Boolean{
        return mList.any({
            it.checked
        })
    }

    fun swapData(list: List<GenreFilter>){
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
            binding!!.cbGenre.setOnClickListener({
                mHandler.clickOnItem(
                        mList.get(adapterPosition).id,
                        binding!!.cbGenre.isChecked)
            })
        }

        override fun onClick(p0: View?) {
            val checkStatus =  !(binding!!.cbGenre.isChecked)
            binding!!.cbGenre.isChecked = checkStatus

            mHandler.clickOnItem(mList.get(adapterPosition).id, checkStatus)
        }
    }
}