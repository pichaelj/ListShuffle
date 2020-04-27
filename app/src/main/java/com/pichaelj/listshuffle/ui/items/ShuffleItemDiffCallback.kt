package com.pichaelj.listshuffle.ui.items

import androidx.recyclerview.widget.DiffUtil
import com.pichaelj.listshuffle.data.ShuffleItem

class ShuffleItemDiffCallback : DiffUtil.ItemCallback<ShuffleItem>() {

    override fun areItemsTheSame(oldItem: ShuffleItem, newItem: ShuffleItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ShuffleItem, newItem: ShuffleItem): Boolean =
        oldItem == newItem
}