package com.demo.developer.deraesw.demomoviewes.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.data.model.MovieSortItem
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.databinding.ItemSortingMovieBinding
import com.demo.developer.deraesw.demomoviewes.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

class SortingMovieAdapter(val mHandler: SortingMovieAdapterInterface):
        RecyclerView.Adapter<SortingMovieAdapter.SortingMovieViewHolder>() {

    private val TAG = SortingMovieAdapter::class.java.simpleName

    private var mList: List<MovieSortItem> = ArrayList();

    interface SortingMovieAdapterInterface {
        fun clickOnItem(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortingMovieViewHolder {
        val binding = ItemSortingMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return SortingMovieViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SortingMovieViewHolder, position: Int) {
        val item = mList.get(position)
        holder.binding?.sorting = item

        if(item.selected){
            holder.binding!!.ivOptionSelected.visibility = View.VISIBLE
        } else {
            holder.binding!!.ivOptionSelected.visibility = View.GONE
        }

        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = mList.size

    fun getItemAt(position : Int) : MovieSortItem = mList.get(position)

    fun swapData(list: List<MovieSortItem>){

        if(mList.isEmpty()){
            mList = list
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = mList.size

                override fun getNewListSize(): Int = list.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mList.get(oldItemPosition).key == list.get(newItemPosition).key
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

    fun changeSelectedItem(position : Int){
        mList.forEachIndexed{
            index, item -> item.selected = (position == index)
        }
        notifyDataSetChanged()
    }

    inner class SortingMovieViewHolder(itemView : View) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener{

        internal var binding : ItemSortingMovieBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition;
            mHandler.clickOnItem(position)
        }
    }
}