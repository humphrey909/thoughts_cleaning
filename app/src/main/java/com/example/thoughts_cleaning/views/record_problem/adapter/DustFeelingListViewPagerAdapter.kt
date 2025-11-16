package com.example.thoughts_cleaning.views.record_problem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.DustFeelingItem
import com.example.thoughts_cleaning.databinding.DustFeelingItemViewPageBinding

class DustFeelingListViewPagerAdapter(private var items: ArrayList<DustFeelingItem>, private val clickListener: DustFeelingItemClickListener) :
    RecyclerView.Adapter<DustFeelingListViewPagerAdapter.PagerViewHolder>() {

//        var prePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = DataBindingUtil.inflate<DustFeelingItemViewPageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.dust_feeling_item_view_page, parent, false
        )

        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) = holder.bind(position, items[position], clickListener)


    override fun getItemCount(): Int = items.size

    inner class PagerViewHolder(val binding: DustFeelingItemViewPageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int, item: DustFeelingItem, clickListener: DustFeelingItemClickListener) = with(binding) {
            this.item = item
            this.position = position
            this.clickListener = clickListener

            binding.dustItem.setOnClickListener {

//                if(prePosition != -1){
//                    items[prePosition].state = !items[prePosition].state
//                    notifyItemChanged(prePosition)
//                }else{
//                }
//                prePosition = position
                item.state = !item.state

                notifyItemChanged(position)

                clickListener.clickListener(item, position)
            }
        }
    }
}

class DustFeelingItemClickListener(val clickListener: (item: DustFeelingItem, position:Int) -> Unit) {
    fun onClick(item: DustFeelingItem, position:Int) = {
        clickListener(item, position)
    }

}