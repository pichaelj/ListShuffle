package com.pichaelj.listshuffle.ui.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.data.ShuffleList
import com.pichaelj.listshuffle.databinding.ShuffleListRvItemBinding

interface ShuffleListSelectedListener {
    fun onListSelected(list: ShuffleList)
}

class ShuffleListsAdapter(
    private val listSelectedListener: ShuffleListSelectedListener
) : ListAdapter<ShuffleList, ShuffleListsAdapter.ViewHolder>(ShuffleListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent, listSelectedListener)

    class ViewHolder private constructor(
        private val binding: ShuffleListRvItemBinding,
        private val listSelectedListener: ShuffleListSelectedListener
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup, listSelectedListener: ShuffleListSelectedListener): ViewHolder {
                val binding = ShuffleListRvItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return ViewHolder(binding, listSelectedListener)
            }
        }

        fun bind(list: ShuffleList) {
            binding.listItemTv.text = list.name

            binding.listItCv.setOnClickListener {
                listSelectedListener.onListSelected(list)
            }
        }
    }
}