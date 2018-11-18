package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.model.CrewItem
import com.demo.developer.deraesw.demomoviewes.databinding.ItemCrewItemBinding

class CrewAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<CrewAdapter.CrewViewHolder>() {
    private val TAG = CrewAdapter::class.java.simpleName

    private var mList: List<CrewItem> = ArrayList();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val binding = ItemCrewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return CrewViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        val item = mList.get(position)

        Log.d(TAG, "item => ${item.name}")
        holder.binding?.crew = item
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = mList.size

    fun getItemAt(position : Int) : CrewItem = mList.get(position)

    fun swapData(list: List<CrewItem>){
        Log.d(TAG, "swapData")
        if(mList.isEmpty()){
            Log.d(TAG, "swapData fill empty")
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

    inner class CrewViewHolder(itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

        internal var binding:ItemCrewItemBinding? = null

        init {
            Log.d(TAG, "Binding")
            binding = DataBindingUtil.bind(itemView)
        }
    }
}