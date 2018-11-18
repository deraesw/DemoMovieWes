package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.model.SortItem
import com.demo.developer.deraesw.demomoviewes.databinding.ItemSortingMovieBinding

class SortingAdapter(val mHandler: SortingMovieAdapterInterface):
        androidx.recyclerview.widget.RecyclerView.Adapter<SortingAdapter.SortingMovieViewHolder>() {

    private val TAG = SortingAdapter::class.java.simpleName

    private var mList: List<SortItem> = ArrayList();

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

    fun getItemAt(position : Int) : SortItem = mList.get(position)

    fun swapData(list: List<SortItem>){

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
            androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener{

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