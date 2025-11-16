package com.example.thoughts_cleaning.views.record_problem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.DustKindItem
import com.example.thoughts_cleaning.databinding.DustKindItemViewPageBinding
import com.example.thoughts_cleaning.databinding.PeopleExItemViewPageBinding

class DustKindListViewPagerAdapter(private var items: ArrayList<DustKindItem>, private val clickListener: DustKindItemClickListener) :
    RecyclerView.Adapter<DustKindListViewPagerAdapter.PagerViewHolder>() {

        var prePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = DataBindingUtil.inflate<DustKindItemViewPageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.dust_kind_item_view_page, parent, false
        )

        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) = holder.bind(position, items[position], clickListener)


    override fun getItemCount(): Int = items.size

    inner class PagerViewHolder(val binding: DustKindItemViewPageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int, item: DustKindItem, clickListener: DustKindItemClickListener) = with(binding) {
            this.item = item
            this.position = position
            this.clickListener = clickListener

            binding.dustItem.setOnClickListener {

                if(prePosition != -1){
                    items[prePosition].state = !items[prePosition].state
                    notifyItemChanged(prePosition)
                }else{


                }
                prePosition = position
                item.state = !item.state

                notifyItemChanged(position)

                clickListener.clickListener(item, position)
            }
        }
    }
}

class DustKindItemClickListener(val clickListener: (item: DustKindItem, position:Int) -> Unit) {
    fun onClick(item: DustKindItem, position:Int) = {
        clickListener(item, position)
    }

}