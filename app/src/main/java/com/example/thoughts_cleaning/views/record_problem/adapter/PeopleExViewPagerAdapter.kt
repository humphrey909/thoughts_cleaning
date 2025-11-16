package com.example.thoughts_cleaning.views.record_problem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.PeopleExItemViewPageBinding

class PeopleExViewPagerAdapter(private var items: ArrayList<String>, private val clickListener: PeopleExItemClickListener) :
    RecyclerView.Adapter<PeopleExViewPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = DataBindingUtil.inflate<PeopleExItemViewPageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.people_ex_item_view_page, parent, false
        )

        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) = holder.bind(items[position], clickListener)


    override fun getItemCount(): Int = items.size

    inner class PagerViewHolder(val binding: PeopleExItemViewPageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: String, clickListener: PeopleExItemClickListener) = with(binding) {
            this.item = item
//            this.next = live[1]
            this.clickListener = clickListener
        }
    }
}

class PeopleExItemClickListener(val clickListener: (view: View, item: Int) -> Unit) {
    fun onClick(view: View, item: Int) = clickListener(view, item)
}