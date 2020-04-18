package com.pichaelj.listshuffle.ui.lists

import androidx.recyclerview.widget.DiffUtil
import com.pichaelj.listshuffle.data.ShuffleList

class ShuffleListDiffCallback : DiffUtil.ItemCallback<ShuffleList>() {

    override fun areItemsTheSame(oldItem: ShuffleList, newItem: ShuffleList): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ShuffleList, newItem: ShuffleList): Boolean =
        oldItem == newItem
}