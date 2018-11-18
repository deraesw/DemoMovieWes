package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.data.entity.ProductionCompany
import com.demo.developer.deraesw.demomoviewes.data.model.CrewItem
import com.demo.developer.deraesw.demomoviewes.databinding.ItemCrewItemBinding
import com.demo.developer.deraesw.demomoviewes.databinding.ItemMovieProductionBinding

class ProductionAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<ProductionAdapter.ProductionViewHolder>() {
    private val TAG = ProductionAdapter::class.java.simpleName

    private var mList: List<ProductionCompany> = ArrayList();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductionViewHolder {
        val binding = ItemMovieProductionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return ProductionViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProductionViewHolder, position: Int) {
        val item = mList.get(position)

        Log.d(TAG, "item => ${item.name}")
        holder.binding?.prod = item
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = mList.size

    fun getItemAt(position : Int) : ProductionCompany = mList.get(position)

    fun swapData(list: List<ProductionCompany>){
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

    inner class ProductionViewHolder(itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

        internal var binding:ItemMovieProductionBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }
    }
}