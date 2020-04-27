package com.pichaelj.listshuffle.ui.lists

import androidx.recyclerview.widget.DiffUtil
import com.pichaelj.listshuffle.ui.lists.views.ExistingListViewModel
import com.pichaelj.listshuffle.ui.lists.views.ShuffleListDataItem

class ShuffleListDiffCallback : DiffUtil.ItemCallback<ShuffleListDataItem>() {

    override fun areItemsTheSame(oldItem: ShuffleListDataItem, newItem: ShuffleListDataItem): Boolean =
        oldItem.list.id == newItem.list.id

    override fun areContentsTheSame(oldItem: ShuffleListDataItem, newItem: ShuffleListDataItem): Boolean =
        oldItem.list == newItem.list
}