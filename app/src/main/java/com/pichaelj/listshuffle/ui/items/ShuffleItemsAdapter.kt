package com.pichaelj.listshuffle.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.data.ShuffleItem
import com.pichaelj.listshuffle.databinding.ShuffleItemRvItemBinding

class ShuffleItemsAdapter : ListAdapter<ShuffleItem, ShuffleItemsAdapter.ViewHolder>(
    ShuffleItemDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    // region ViewHolder

    class ViewHolder private constructor(
        private val binding: ShuffleItemRvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ShuffleItemRvItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return ViewHolder(binding)
            }
        }

        fun bind(item: ShuffleItem) {
            binding.itemLabelTv.text = item.label
        }
    }

    // endregion
}