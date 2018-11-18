package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.databinding.ItemCastingItemBinding

class CastingAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<CastingAdapter.CastingViewHolder>() {
    private val TAG = CastingAdapter::class.java.simpleName

    private var mList: List<CastingItem> = ArrayList();


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastingViewHolder {
        val binding = ItemCastingItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return CastingViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CastingViewHolder, position: Int) {
        val item = mList.get(position)

        holder.binding?.casting = item
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = mList.size

    fun getItemAt(position : Int) : CastingItem = mList.get(position)

    fun swapData(list: List<CastingItem>){

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

    inner class CastingViewHolder(itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

        internal var binding:ItemCastingItemBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }
    }
}