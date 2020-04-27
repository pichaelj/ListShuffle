package com.pichaelj.listshuffle.ui.items

import androidx.recyclerview.widget.DiffUtil
import com.pichaelj.listshuffle.ui.items.views.ShuffleItemDataItem

class ShuffleItemDiffCallback : DiffUtil.ItemCallback<ShuffleItemDataItem>() {

    override fun areItemsTheSame(oldItem: ShuffleItemDataItem, newItem: ShuffleItemDataItem): Boolean =
        oldItem.item.id == newItem.item.id

    override fun areContentsTheSame(oldItem: ShuffleItemDataItem, newItem: ShuffleItemDataItem): Boolean =
        oldItem.item == newItem.item && oldItem.viewType == newItem.viewType
}